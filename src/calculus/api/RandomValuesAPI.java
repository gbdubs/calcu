package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.Content;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;

public class RandomValuesAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	private static Entity getCursorStorage(){
		Key cursorKey = KeyFactory.createKey("CursorStorage", "oneandonly");
		Entity cursorStorage;
		try {
			cursorStorage = datastore.get(cursorKey);
		} catch (EntityNotFoundException e) {
			cursorStorage = new Entity(cursorKey);
		}
		return cursorStorage;
	}
	
	// COST: 3 READ, 1 WRITE
	public static String randomTag() {
		List<Entity> entities = getNEntitiesOfType(1, "Tag");
		return (String) entities.get(0).getProperty("name");
	}
	
	// COST: 2 + N Read, 1 WRITE
	public static List<String> randomTags(int n) {
		List<Entity> entities = getNEntitiesOfType(n, "Tag");
		List<String> result = new ArrayList<String>();
		for(Entity e : entities){
			result.add((String) e.getProperty("name"));
		}
		return result;
	}
	
	// COST: 3 READ, 1 WRITE
	public static Content randomContent() {
		List<Entity> entities = getNEntitiesOfType(1, "Content");
		return ContentAPI.instantiateContent(entities.get(0));
	}

	// COST: 2 + N READ, 1 WRITE
	public static List<Content> randomContents(int n) {
		List<Entity> entities = getNEntitiesOfType(n, "Content");
		List<Content> result = new ArrayList<Content>();
		for(Entity e : entities){
			result.add(ContentAPI.instantiateContent(e));
		}
		return result;
	}
	
	public static List<String> getRandomPhenotypes(int n){
		List<Entity> entities = getNEntitiesOfType(n, "Phenotype");
		List<String> result = new ArrayList<String>();
		for(Entity e : entities){
			result.add(e.getKey().getName());
		}
		return result;
	}
	
	private static List<Entity> getNEntitiesOfType(int n, String type){
		Entity cursorStorage = getCursorStorage();
		String cursorString = (String) cursorStorage.getProperty(type + "Cursor");
		QueryResultList<Entity> list;
		if (cursorString == null){
			Query q = new Query(type);
			PreparedQuery pq = datastore.prepare(q);
			list = pq.asQueryResultList(FetchOptions.Builder.withLimit(n));
			Cursor cursor = list.getCursor();
			cursorString = cursor.toWebSafeString();
			cursorStorage.setUnindexedProperty(type + "cursor", cursorString);
		} else {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			Query q = new Query(type);
			PreparedQuery pq = datastore.prepare(q);
			list = pq.asQueryResultList(FetchOptions.Builder.withLimit(n).startCursor(cursor));
			Cursor endCursor = list.getCursor();
			if (list.size() < n){
				q = new Query(type);
				pq = datastore.prepare(q);
				pq.asQueryResultList(FetchOptions.Builder.withLimit(n));
				endCursor = list.getCursor();
			}
			cursorString = endCursor.toWebSafeString();
			cursorStorage.setUnindexedProperty(type + "Cursor", cursorString);
		}
		datastore.put(cursorStorage);
		
		return list;
	}
}
