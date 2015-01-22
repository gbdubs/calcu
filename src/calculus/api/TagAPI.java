package calculus.api;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class TagAPI {

	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	
	public static void addNewContentToTag(String contentUuid, String tag){
		Key key = KeyFactory.createKey("Tag", tag);
		Entity entity = new Entity(key);
		List<String> uuids = new ArrayList<String>();
		try {
			entity = datastoreService.get(key);
			uuids = (List<String>) entity.getProperty("matchingContent");
		} catch (EntityNotFoundException e) {
			entity.setProperty("name", tag);
		}
		uuids.add(contentUuid);
		entity.setProperty("matchingContent", uuids);
		datastoreService.put(entity);
	}

	public static List<String> getUuidsOfTag(String tag){
		Key key = KeyFactory.createKey("Tag", tag);
		Entity entity;
		try {
			entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			return new ArrayList<String>();
		}
		return (List<String>) entity.getProperty("matchingContent");
	}
}
