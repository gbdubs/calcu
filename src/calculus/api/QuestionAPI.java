package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.Question;

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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class QuestionAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static Filter questionFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "question");
	private static Filter answerFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "answer");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter unsubmittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, false);
	private static Filter anonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, true);
	private static Filter notAnonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, false);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	private static Filter notViewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, false);
	
	public static String createQuestionFromRequest(HttpServletRequest req){
		
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		if (title == null || title == "") title = "[Un-named Question]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[This author was so caught up in the existential crisis of life, they realized that the only true question is 'why?', and can best be represented by not putting in anything in their question's body]";
		Text wrappedBody = new Text(body);
		String tags = req.getParameter("tagsInput");
		
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setProperty("uuid", uuid);
		entity.setProperty("contentType", "question");
		entity.setProperty("creatorUserId", UserServiceFactory.getUserService().getCurrentUser().getUserId());
		entity.setProperty("createdAt", time);
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", "/question/" + uuid);
		entity.setProperty("tags", tags);
		entity.setProperty("requests", 1);
		
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
		if (title == null || title == "") title = "[Un-named Question]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[This author was so caught up in the existential crisis of life, they realized that the only true question is 'why?', and can best be represented by not putting in anything in their question's body]";	
		Text wrappedBody = new Text(body);
		String tags = req.getParameter("tagsInput");
		
		entity.setProperty("createdAt", time);
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("tags", tags);

		datastore.put(entity);
		
		if (submitted && viewable){
			String[] tagList = tags.split(",");
			for (String t : tagList){
				if (t.length() > 0) TagAPI.addNewContentToTag(uuid, t);
			}
		}
		
		return uuid;
	}
	
	public static List<Answer> getAnswersForQuestion(Question q){
		List<Answer> list = new ArrayList<Answer>();
		String qUuid = q.getUuid();
		Filter questionFilter = new FilterPredicate("parentUuid", FilterOperator.EQUAL, qUuid);
		Filter compositeFilter = CompositeFilterOperator.and(questionFilter, answerFilter, viewableFilter, submittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Answer a = new Answer(result);
			list.add(a);
		}
		return list;	
	}

	public static List<Question> getUnsubmittedQuestions(User user) {
		List<Question> list = new ArrayList<Question>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, user.getUserId());
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, questionFilter, unsubmittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Question q = new Question(result);
			list.add(q);
		}
		return list;
	}

	public static List<Question> getSubmittedQuestions(User user) {
		List<Question> list = new ArrayList<Question>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, user.getUserId());
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, questionFilter, submittedFilter, notAnonymousFilter, viewableFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Question q = new Question(result);
			list.add(q);
		}
		return list;
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
}
