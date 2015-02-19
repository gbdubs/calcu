package calculus.api;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.Content;
import calculus.models.Question;
import calculus.utilities.Cleaner;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.UserServiceFactory;

public class QuestionAPI {
	
	private static Filter questionFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "question");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	
	public static String createQuestionFromRequest(HttpServletRequest req){
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		title = Cleaner.autoclave(title);
		if (title == null || title == "") title = "[Un-named Question]";
		
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body == "") body = "[This author was so caught up in the existential crisis of life, they realized that the only true question is 'why?', and can best be represented by not putting in anything in their question's body]";
		Text wrappedBody = new Text(body);
		
		String tags = req.getParameter("tagsInput");
		tags = Cleaner.cleanHtml(tags);
		
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setUnindexedProperty("uuid", uuid);
		entity.setProperty("contentType", "question");
		entity.setProperty("creatorUserId", UserServiceFactory.getUserService().getCurrentUser().getUserId());
		entity.setProperty("createdAt", time);
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setUnindexedProperty("url", "/question/" + uuid);
		entity.setUnindexedProperty("tags", tags);
		entity.setProperty("requests", 1);
		entity.setProperty("karma", 1);
		
		datastore.put(entity);
		
		if (submitted && viewable){
			String[] tagList = tags.split(",");
			for (String t : tagList){
				if (t.length() > 0) TagAPI.addNewContentToTag(uuid, t);
			}
		}
		
		return uuid;
	}

	public static String updateQuestionFromRequest(HttpServletRequest req){
		
		String uuid = (String) req.getParameter("uuid");
		Question q = new Question(uuid);
		Entity entity = q.getEntity();
		
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		title = Cleaner.autoclave(title);
		if (title == null || title == "") title = "[Un-named Question]";
		
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body == "") body = "[This author was so caught up in the existential crisis of life, they realized that the only true question is 'why?', and can best be represented by not putting in anything in their question's body]";	
		Text wrappedBody = new Text(body);
		
		String tags = req.getParameter("tagsInput");
		tags = Cleaner.cleanHtml(tags);
		
		entity.setProperty("createdAt", time);
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setUnindexedProperty("tags", tags);

		datastore.put(entity);
		
		if (submitted && viewable){
			String[] tagList = tags.split(",");
			for (String t : tagList){
				if (t.length() > 0) TagAPI.addNewContentToTag(uuid, t);
			}
		}
		
		return uuid;
	}

	public static void addQuestionContext(HttpServletRequest req, Question q) {
		req.setAttribute("question", q);
	}

	public static String createOrUpdateQuestionFromRequest(HttpServletRequest req) {
		if (req.getParameter("uuid") == "" || req.getParameter("uuid") == null){
			return QuestionAPI.createQuestionFromRequest(req);
		} else {
			return QuestionAPI.updateQuestionFromRequest(req);
		}
	}

	public static Question getAnswerableQuestion(String userId) {
		Filter notMe = new FilterPredicate("creatorUserId", FilterOperator.NOT_EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(notMe, questionFilter, viewableFilter, submittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("creatorUserId").addSort("createdAt", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(query);
		List<String> skipped = UserPrivateInfoAPI.getUserSkippedContentUuids(userId);
		List<String> answered= UserPrivateInfoAPI.getUserAnsweredContentUuids(userId);
		for (Entity result : pq.asIterable()) {
			String uuid = (String) result.getProperty("uuid");
			if (!skipped.contains(uuid) && !answered.contains(uuid)){
				return new Question(result);
			}
		}
		return null;
	}

	public static String createNewQuestionFromUpload(String title, String body, String tags, String site) {
		
		body = Cleaner.cleanHtml(body);
		title = Cleaner.autoclave(title);
		tags = Cleaner.cleanHtml(tags);
		
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		
		boolean anonymous = true;
		boolean submitted = true;
		boolean viewable = true;
		
		Text wrappedBody = new Text(body);
		
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setUnindexedProperty("uuid", uuid);
		entity.setProperty("contentType", "question");
		entity.setProperty("creatorUserId", Content.scrapingUserProfileId);
		entity.setProperty("createdAt", time);
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setUnindexedProperty("url", "/question/" + uuid);
		entity.setUnindexedProperty("tags", tags);
		entity.setProperty("requests", 1);
		entity.setProperty("karma", 1);
		
		datastore.put(entity);

		String[] tagList = tags.split(",");
		for (String t : tagList){
			if (t.length() > 0) TagAPI.addNewContentToTag(uuid, t);
		}
		
		return uuid;
	}

	public static List<Answer> getAnswersForQuestion(Question q){
		return q.getAnswers();
	}
	
	public static void addAnswerToQuestion(String qUuid, String answerUuid){
		Question q = new Question(qUuid);
		q.addAnswer(answerUuid);
	}
}
