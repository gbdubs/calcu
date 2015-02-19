package calculus.api;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.TextContent;
import calculus.utilities.Cleaner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.UserServiceFactory;

public class TextContentAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static String createTextContentFromRequest(HttpServletRequest req){
		
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		title = Cleaner.autoclave(title);
		if (title == null || title == "") title = "[Un-named Text Content]";
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body == "") body = "[No body was provided for this Content]";
		Text wrappedBody = new Text(body);
		String tags = req.getParameter("tagsInput");
		tags = Cleaner.cleanHtml(tags);

		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setUnindexedProperty("uuid", uuid);
		entity.setProperty("contentType", "textContent");
		entity.setProperty("creatorUserId", UserServiceFactory.getUserService().getCurrentUser().getUserId());
		entity.setProperty("createdAt", time);
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setUnindexedProperty("url", "/text-content/" + uuid);
		entity.setUnindexedProperty("tags", tags);
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

    public static String updateTextContentFromRequest(HttpServletRequest req){
		
		String uuid = (String) req.getParameter("uuid");
		TextContent tc = new TextContent(uuid);
		Entity entity = tc.getEntity();
		
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		title = Cleaner.autoclave(title);
		if (title == null || title == "") title = "[Un-named Text Context]";
		
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body == "") body = "[No body was provided for this Content]";	
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
    
    public static String createOrUpdateTextContentFromRequest(HttpServletRequest req) {
		if (req.getParameter("uuid") == "" || req.getParameter("uuid") == null) {
			return createTextContentFromRequest(req);
		} else {
			return updateTextContentFromRequest(req);
		}
	}

	public static void addTextContentContext(HttpServletRequest req, TextContent tc) {
		req.setAttribute("textContent", tc);
	}

	public static TextContent[] getTextContentCalibrationForUser(String userId) {
		TextContent[] result = new TextContent[2];
		Filter contentFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "textContent");
		Query q = new Query("Content").setFilter(contentFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(q);
		int i = 0;
		for (Entity entity : pq.asIterable()){
			result[i++] = new TextContent(entity);
			if (i >= 2) return result;
		}
		return result;
	}

	public static List<Answer> getAnswersForTextContent(TextContent tc){
		return tc.getAnswers();
	}
	
	public static void addAnswerToTextContent(String tcUuid, String answerUuid){
		TextContent tc = new TextContent(tcUuid);
		tc.addAnswer(answerUuid);
	}
}
