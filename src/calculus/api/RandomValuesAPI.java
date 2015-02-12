package calculus.api;

import calculus.models.Content;

import com.google.appengine.api.datastore.AsyncDatastoreService;
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
	
	
	public static String randomTag() {
		Entity cursorStorage = getCursorStorage();
		String cursorString = (String) cursorStorage.getProperty("tagCursor");
		QueryResultList<Entity> list;
		if (cursorString == null){
			Query q = new Query("Tag");
			PreparedQuery pq = datastore.prepare(q);
			list = pq.asQueryResultList(FetchOptions.Builder.withLimit(1));
			Cursor cursor = list.getCursor();
			cursorString = cursor.toWebSafeString();
			cursorStorage.setUnindexedProperty("tagCursor", cursorString);
		} else {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			Query q = new Query("Tag");
			PreparedQuery pq = datastore.prepare(q);
			list = pq.asQueryResultList(FetchOptions.Builder.withLimit(1).startCursor(cursor));
			Cursor endCursor = list.getCursor();
			cursorString = endCursor.toWebSafeString();
			cursorStorage.setUnindexedProperty("tagCursor", cursorString);
		}
		datastore.put(cursorStorage);
		
		Entity e = list.get(0);
		String name = (String) e.getProperty("name");
		return name;
	}
	
	
	public static Content randomContent() {
		
		Entity cursorStorage = getCursorStorage();
		String cursorString = (String) cursorStorage.getProperty("contentCursor");
		QueryResultList<Entity> list;
		if (cursorString == null){
			Query q = new Query("Content");
			PreparedQuery pq = datastore.prepare(q);
			list = pq.asQueryResultList(FetchOptions.Builder.withLimit(1));
			Cursor cursor = list.getCursor();
			cursorString = cursor.toWebSafeString();
			cursorStorage.setUnindexedProperty("contentCursor", cursorString);
		} else {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			Query q = new Query("Content");
			PreparedQuery pq = datastore.prepare(q);
			list = pq.asQueryResultList(FetchOptions.Builder.withLimit(1).startCursor(cursor));
			Cursor endCursor = list.getCursor();
			cursorString = endCursor.toWebSafeString();
			cursorStorage.setUnindexedProperty("contentCursor", cursorString);
		}
		datastore.put(cursorStorage);
		
		Entity e = list.get(0);
		return new Content(e);
	}
}
