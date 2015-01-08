package calculus.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Content;
import calculus.utilities.MenuItem;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;

public class BookmarksAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
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

	public static void addUserBookmarksToRequest(HttpServletRequest req, User user){
		
		if (user != null){
			Entity userPrivateInfo = UserDatastoreAPI.getOrCreateUserPrivateInfo(user.getUserId());
			List<String> bookmarks = (List<String>) userPrivateInfo.getProperty("bookmarks");
			List<MenuItem> bookmarksToDisplay = new ArrayList<MenuItem>();
			List<String> bookmarkUuids = new ArrayList<String>();
			if (bookmarks != null){
				for(String  b : bookmarks){
					String contentType = Content.getContentType(b);
					Content c;
					try {
						c = new Content(datastore.get(KeyFactory.createKey("Content", b)), contentType);
						bookmarksToDisplay.add(new MenuItem(c));
						bookmarkUuids.add(c.getUuid());
					} catch (EntityNotFoundException e) {
						// TODO Skip?
					}
				}
			}
			req.setAttribute("bookmarksMenu", bookmarksToDisplay);
			req.setAttribute("bookmarkUuids", bookmarkUuids);
		} else {
			MenuItem[] bookmarks = new MenuItem[2];
			bookmarks[0] = new MenuItem("#", "", "You can store Bookmarks of your favorite", "", "", "success", "fa-bank", "");
			bookmarks[1] = new MenuItem("#", "", "materials, practice problems and questions", "", "", "info", "fa-question", "");
			req.setAttribute("bookmarksMenu", bookmarks);
		}
	}
}
