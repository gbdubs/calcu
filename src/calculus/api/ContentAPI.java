package calculus.api;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ContentAPI {


	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	private static Filter compositeFilter = CompositeFilterOperator.and(viewableFilter, submittedFilter);

	
	public static List<Content> getNewContent(int i) {
		Query q = new Query("Content").addSort("createdAt", SortDirection.DESCENDING).setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i));
		List<Content> results = new ArrayList<Content>();
		for (Entity e : entities){
			results.add(new Content(e));
		}
		return results;
	}
	
	public static List<Content> getBestContent(int i) {
		Query q = new Query("Content").addSort("karma", SortDirection.DESCENDING).setFilter(compositeFilter);
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
		Query q = new Query("Content").addSort("karma", SortDirection.DESCENDING).setFilter(compositeFilter);
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
			return "info";
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

	public static String getContentAuthorId(String contentUuid) {
		try {
			Content c = new Content(contentUuid);
			return c.getAuthor().getUserId();
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public static List<Content> getContentWithAuthor(String userId, int maxToDisplay, int offset) {
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter totalFilter = CompositeFilterOperator.and(userFilter, compositeFilter);
		Query q = new Query("Content").addSort("createdAt", SortDirection.DESCENDING).setFilter(totalFilter);
		
		List<Entity> queryResults = datastore.prepare(q).asList(withLimit(maxToDisplay).offset(offset));
		System.out.println(queryResults.toString());
		List<Content> content = new ArrayList<Content>();
		for (Entity e : queryResults){
			content.add(new Content(e));
		}
		return content;
	}
}
