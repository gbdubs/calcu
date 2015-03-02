package calculus.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Content;
import calculus.utilities.MenuItem;
import calculus.utilities.UrlGenerator;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class BookmarksAPI {
	
	public static void addBookmarkForUser(String contentUuid, String userId){
		List<String> uuids = UserPrivateInfoAPI.getBookmarkUuids(userId);
		List<Text> jsons = UserPrivateInfoAPI.getBookmarkJsons(userId);
		
		if (!uuids.contains(contentUuid)) {
			try {
				String jsonRepresentation = new MenuItem(new Content(contentUuid)).toJson();
				uuids.add(contentUuid);
				jsons.add(new Text(jsonRepresentation));
				UserPrivateInfoAPI.setBookmarks(userId, uuids, jsons);
			} catch (EntityNotFoundException e) {
				// We needn't do anything if the bookmark points to content that does not exist!
			}	
		}
	}
	
	public static void deleteBookmarkForUser(String contentUuid, String userId){
		List<String> uuids = UserPrivateInfoAPI.getBookmarkUuids(userId);
		List<Text> jsons = UserPrivateInfoAPI.getBookmarkJsons(userId);
		
		if (uuids.contains(contentUuid)) {
			int index = uuids.indexOf(contentUuid);
			uuids.remove(contentUuid);
			jsons.remove(index);
			UserPrivateInfoAPI.setBookmarks(userId, uuids, jsons);
		}
	}

	public static void addUserBookmarksToRequest(HttpServletRequest req, User user){
		
		if (user != null){
			List<String> bookmarkUuids = (List<String>) UserPrivateInfoAPI.getBookmarkUuids(user.getUserId());
			List<Text> bookmarkJsons = (List<Text>) UserPrivateInfoAPI.getBookmarkJsons(user.getUserId());
			
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
		} else {
			List<MenuItem> menuItems = getLoggedOutBookmarks();
			req.setAttribute("bookmarksMenu", menuItems);
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
		return UserPrivateInfoAPI.getBookmarkUuids(userId);
	}
}
