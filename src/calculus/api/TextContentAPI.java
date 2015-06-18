package calculus.api;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.Content;
import calculus.models.TextContent;
import calculus.utilities.Cleaner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
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
		if (title == null || title.equals("")) title = "[Un-named Text Content]";
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body.equals("")) body = "[No body was provided for this Content]";
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
		TextContent tc;
		try {
			tc = new TextContent(uuid);
		} catch (EntityNotFoundException e) {
			// If the text content does not exist, we need not update it.
			return null;
		}
		Entity entity = tc.getEntity();
		
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		title = Cleaner.autoclave(title);
		if (title == null || title.equals("")) title = "[Un-named Text Context]";
		
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body.equals("")) body = "[No body was provided for this Content]";	
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
		if (req.getParameter("uuid").equals("") || req.getParameter("uuid") == null) {
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
		
		try {
			TextContent tc = new TextContent(tcUuid);
			tc.addAnswer(answerUuid);
		} catch (EntityNotFoundException e) {
			// If the text content does not exist, we cannot add an answer to it.
			return;
		}
	}

	public static String createNewTextContentFromUpload(String title, String body, String tags, String source) {
		
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
		entity.setProperty("contentType", "textContent");
		entity.setProperty("creatorUserId", "Administrator");
		entity.setProperty("createdAt", time);
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setUnindexedProperty("url", "/text-content/" + uuid);
		entity.setUnindexedProperty("tags", tags);
		entity.setProperty("karma", 1);
		entity.setUnindexedProperty("source", source);
		
		datastore.put(entity);

		String[] tagList = tags.split(",");
		for (String t : tagList){
			if (t.length() > 0) TagAPI.addNewContentToTag(uuid, t);
		}
		
		return uuid;
		
	}
}
