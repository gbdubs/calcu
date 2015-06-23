package calculus.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.utilities.MenuItem;
import calculus.utilities.UrlGenerator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class BookmarksAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void addBookmarkForUser(String contentUuid, String userId){
		Entity bookmarkInfo = getUserBookmarksEntity(userId);
		List<String> uuids = getBookmarkUuids(bookmarkInfo);
		List<Text> jsons = getBookmarkJsons(bookmarkInfo);
		int numberUnread = getNumberOfUnreadBookmarks(bookmarkInfo);
		
		if (!uuids.contains(contentUuid)) {
			try {
				String jsonRepresentation = new MenuItem(ContentAPI.instantiateContent(contentUuid)).toJson();
				uuids.add(contentUuid);
				jsons.add(new Text(jsonRepresentation));
				numberUnread++;
				setBookmarks(bookmarkInfo, uuids, jsons, numberUnread);
			} catch (EntityNotFoundException e) {
				// We needn't do anything if the bookmark points to content that does not exist!
			}	
		}
	}
	
	public static void deleteBookmarkForUser(String contentUuid, String userId){
		Entity bookmarkInfo = getUserBookmarksEntity(userId);
		List<String> uuids = getBookmarkUuids(bookmarkInfo);
		List<Text> jsons = getBookmarkJsons(bookmarkInfo);
		int numberUnread = getNumberOfUnreadBookmarks(bookmarkInfo);
		
		if (uuids.contains(contentUuid)) {
			int index = uuids.indexOf(contentUuid);
			uuids.remove(contentUuid);
			jsons.remove(index);
			setBookmarks(bookmarkInfo, uuids, jsons, numberUnread);
		}
	}

	public static void markAllBookmarksRead(String userId) {
		Entity userBookmarks = getUserBookmarksEntity(userId);
		userBookmarks.setUnindexedProperty("unreadBookmarks", new Long(0));
		datastore.put(userBookmarks);
	}
	
	private static Entity getUserBookmarksEntity(String userId){
		Key bookmarksKey = KeyFactory.createKey("UserBookmarks", userId);
		Entity e = new Entity(bookmarksKey);
		try{
			e = datastore.get(bookmarksKey);
		} catch (EntityNotFoundException enfe){
			
		}
		return e;
	}
	
	private static List<String> getBookmarkUuids(Entity bookmarkInfo) {
		List<String> result = (List<String>) bookmarkInfo.getProperty("bookmarkUuids");
		if (result != null) return result;
		return new ArrayList<String>();
	}

	private static List<Text> getBookmarkJsons (Entity bookmarkInfo) {
		List<Text> result = (List<Text>) bookmarkInfo.getProperty("bookmarkJsons");
		if (result != null) return result;
		return new ArrayList<Text>();
	}
	
	private static int getNumberOfUnreadBookmarks(Entity bookmarkInfo) {
		Long l = (Long) bookmarkInfo.getProperty("unreadBookmarks");
		if (l == null) {
			List<Text> texts = (List<Text>) bookmarkInfo.getProperty("bookmarkJsons");
			if (texts == null) return 0;
			return texts.size();
		}
		return l.intValue();
	}

	private static void setBookmarks(Entity bookmarkInfo, List<String> bookmarkUuids, List<Text> bookmarkJsons, int numberUnread) {
		bookmarkInfo.setUnindexedProperty("bookmarkUuids", bookmarkUuids);
		bookmarkInfo.setUnindexedProperty("bookmarkJsons", bookmarkJsons);
		bookmarkInfo.setUnindexedProperty("unreadBookmarks", new Long(numberUnread));
		datastore.put(bookmarkInfo);
	}

	public static void addUserBookmarksToRequest(HttpServletRequest req, User user){
		
		if (user != null){
			String userId = user.getUserId();
			Entity bookmarkInfo = getUserBookmarksEntity(userId);
			List<String> bookmarkUuids = getBookmarkUuids(bookmarkInfo);
			List<Text> bookmarkJsons = getBookmarkJsons(bookmarkInfo);
			int numberUnread = getNumberOfUnreadBookmarks(bookmarkInfo);
			
			List<MenuItem> bookmarksToDisplay = new ArrayList<MenuItem>();
			if (bookmarkJsons != null){
				// Traversing through backwards to put bookmarks in their most recent order
				for(int i = bookmarkJsons.size() - 1; i >= 0; i--){
					String json = bookmarkJsons.get(i).getValue();
					MenuItem toAdd = MenuItem.fromJson(json);
					bookmarksToDisplay.add(toAdd);
				}
			} 
			if (bookmarkJsons == null){
				bookmarksToDisplay = getEmptyBookmarkList(user);
			}
			req.setAttribute("bookmarksMenu", bookmarksToDisplay);
			req.setAttribute("bookmarkUuids", bookmarkUuids);
			req.setAttribute("unreadBookmarks", numberUnread);
		} else {
			List<MenuItem> menuItems = getLoggedOutBookmarks();
			req.setAttribute("bookmarksMenu", menuItems);
			req.setAttribute("unreadBookmarks", menuItems.size());
		}
	}

	private static List<MenuItem> getEmptyBookmarkList(User user){
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		menuItems.add(new MenuItem()
		    .withUrl(UrlGenerator.profileUrl(user) + "#bookmarks")
			.withTitle("You don't currently have any bookmarks")
			.withDescription("To bookmark some content, when you are on the content page, simply select the empty bookmark icon.")
			.withColor("danger")
			.withIcon("fa-bookmark")
		);
		return menuItems;
	}

	private static List<MenuItem> getLoggedOutBookmarks() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		menuItems.add(new MenuItem()
			.withTitle("Store your Favorites")
			.withDescription("You can bookmark any content on the site!")
			.withColor("success")
			.withIcon("fa-bank")
		);
		
		menuItems.add(new MenuItem()
		    .withTitle("Save For Later")
		    .withDescription("Come back to a problem when you are ready.")
		    .withColor("info")
		    .withIcon("fa-clock-o")
	    );
		
		menuItems.add(new MenuItem()
	    	.withTitle("Learning From Mistakes")
	    	.withDescription("Revisiting a problem can help you solve it.")
	    	.withColor("warning")
	    	.withIcon("fa-lightbulb-o")
	    );
		
		return menuItems;
	}

	public static List<String> getUserBookmarkUuids(String userId) {
		Entity e = getUserBookmarksEntity(userId);
		return getBookmarkUuids(e);
	}
}
