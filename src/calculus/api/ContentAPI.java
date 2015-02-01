package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.Content;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ContentAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<Content> getNewContent(int i) {
		Query q = new Query("Content").addSort("createdAt", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i));
		List<Content> results = new ArrayList<Content>();
		for (Entity e : entities){
			results.add(new Content(e));
		}
		return results;
	}
	
	public static List<Content> getBestContent(int i) {
		Query q = new Query("Content").addSort("karma", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i));
		List<Content> results = new ArrayList<Content>();
		for (Entity e : entities){
			results.add(new Content(e));
		}
		return results;
	}
	
	public static List<Content> getSuggestedContent(int i, String userId) {
		return getNewContent(i);
	}
	
	//TODO: This
	public static List<Content> getRandomContent(int i) {
		Query q = new Query("Content").addSort("uuid", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i));
		List<Content> results = new ArrayList<Content>();
		for (Entity e : entities){
			results.add(new Content(e));
		}
		return results;
	}

	public static String getContentType(String uuid) throws EntityNotFoundException{
		if (uuid == "" || uuid == null) return null;
		Key contentKey = KeyFactory.createKey("Content", uuid);
		Entity entity = datastore.get(contentKey);
		return (String) entity.getProperty("contentType");
	}

	public static void setInvisible(String uuid){
		Key contentKey = KeyFactory.createKey("Content", uuid);
		Entity entity;
		try {
			entity = datastore.get(contentKey);
			entity.setProperty("viewable", false);
			datastore.put(entity);
		} catch (EntityNotFoundException e) {
			// Don't worry if it doesn't exist, we need not make it less visible!
		}
	}

	public static String getBoxColor(String contentType){
		if (contentType.equals("question")){
			return "primary";
		} else if (contentType.equals("answer")){
			return "danger";
		} else if (contentType.equals("textContent")){
			return "warning";
		} else if (contentType.equals("practiceProblem")){
			return "success";
		} else {
			return "default";
		}
	}

	public static String getBoxIcon(String contentType){
		if (contentType.equals("question")){
			return "fa-question";
		} else if (contentType.equals("answer")){
			return "fa-lightbulb-o";
		} else if (contentType.equals("textContent")){
			return "fa-newspaper-o";
		} else if (contentType.equals("practiceProblem")){
			return "fa-pencil";
		} else {
			return "fa-line-chart";
		}
	}

	public static void setVisible(String uuid) {
		Key contentKey = KeyFactory.createKey("Content", uuid);
		Entity entity;
		try {
			entity = datastore.get(contentKey);
			entity.setProperty("viewable", true);
			datastore.put(entity);
		} catch (EntityNotFoundException e) {
			// Don't worry if it doesn't exist, we need not make it less visible!
		}
	}

	public static void updateKarma(String contentUuid, int karma) {
		Key contentKey = KeyFactory.createKey("Content", contentUuid);
		Entity entity;
		try {
			entity = datastore.get(contentKey);
			entity.setProperty("karma", karma);
			datastore.put(entity);
		} catch (EntityNotFoundException e) {
			// Don't worry if it doesn't exist, we need not make it less visible!
		}
	}


}
