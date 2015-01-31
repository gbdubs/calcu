package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.models.TextContent;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class TextContentAPI {
	private static Filter textContentFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "textContent");
	private static Filter answerFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "answer");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter unsubmittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, false);
	private static Filter notAnonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, false);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	
	public static String createTextContentFromRequest(HttpServletRequest req){
		
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		System.out.println("Title: " + title);
		if (title == null || title == "") title = "[Un-named Text Content]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[No body was provided for this Content]";
		Text wrappedBody = new Text(body);
		String tags = req.getParameter("tagsInput");
		

		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setProperty("uuid", uuid);
		entity.setProperty("contentType", "textContent");
		entity.setProperty("creatorUserId", UserServiceFactory.getUserService().getCurrentUser().getUserId());
		entity.setProperty("createdAt", time);
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", "/text-content/" + uuid);
		entity.setProperty("tags", tags);
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
		if (title == null || title == "") title = "[Un-named Text Context]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[No body was provided for this Content]";	
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
    
    public static List<TextContent> getUnsubmittedTextContent(String userId) {
		List<TextContent> list = new ArrayList<TextContent>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, textContentFilter, unsubmittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			TextContent q = new TextContent(result);
			list.add(q);
		}
		return list;
	}

	public static List<TextContent> getSubmittedTextContent(String userId) {
		List<TextContent> list = new ArrayList<TextContent>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, textContentFilter, submittedFilter, notAnonymousFilter, viewableFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			TextContent q = new TextContent(result);
			list.add(q);
		}
		return list;
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
	
	public static List<Answer> getAnswersForTextContent(TextContent tc){
		List<Answer> list = new ArrayList<Answer>();
		String tcUuid = tc.getUuid();
		Filter contentFilter = new FilterPredicate("parentUuid", FilterOperator.EQUAL, tcUuid);
		Filter compositeFilter = CompositeFilterOperator.and(contentFilter, answerFilter, viewableFilter, submittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Answer a = new Answer(result);
			list.add(a);
		}
		return list;	
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


}
