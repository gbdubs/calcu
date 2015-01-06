package calculus.api;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

public class BookmarksAPI {

	public static void addBookmarkForUser(String contentUuid, String userId){
		
		Entity userPrivateInfo = UserDatastoreAPI.getOrCreateUserPrivateInfo(userId);
		
		List<String> bookmarks = (List<String>) userPrivateInfo.getProperty("bookmarks");
		
		if (bookmarks == null) bookmarks = new ArrayList<String>();
		
		if (!bookmarks.contains(contentUuid)){
			bookmarks.add(contentUuid);
			userPrivateInfo.setProperty("bookmarks", bookmarks);
			DatastoreServiceFactory.getDatastoreService().put(userPrivateInfo);
		}
	}
	
	public static void deleteBookmarkForUser(String contentUuid, String userId){
		
		Entity userPrivateInfo = UserDatastoreAPI.getOrCreateUserPrivateInfo(userId);
		
		List<String> bookmarks = (List<String>) userPrivateInfo.getProperty("bookmarks");
		
		if (bookmarks == null) return;
		
		if (bookmarks.contains(contentUuid)){
			bookmarks.remove(contentUuid);
			userPrivateInfo.setProperty("bookmarks", bookmarks);
			DatastoreServiceFactory.getDatastoreService().put(userPrivateInfo);
		}
	}
	
}
