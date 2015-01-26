package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.Content;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
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
}
