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
				String profileBookmarksUrl = UrlGenerator.profileUrl(user) + "#bookmarks";
				bookmarksToDisplay.add(new MenuItem(
						profileBookmarksUrl, "", "You don't currently have any bookmarks", "To bookmark some content, when you are on the content page, simply select the empty bookmark icon.", "", "", "info", "fa-lightbulb-o",""
				));
			}
			req.setAttribute("bookmarksMenu", bookmarksToDisplay);
			req.setAttribute("bookmarkUuids", bookmarkUuids);
		} else {
			MenuItem[] bookmarks = new MenuItem[2];
			bookmarks[0] = new MenuItem("#", "", "", "You can store Bookmarks of your favorite", "", "", "success", "fa-bank", "");
			bookmarks[1] = new MenuItem("#", "", "", "materials, practice problems and questions", "", "", "info", "fa-question", "");
			req.setAttribute("bookmarksMenu", bookmarks);
		}
	}

	public static List<String> getUserBookmarkUuids(String userId) {
		return UserPrivateInfoAPI.getBookmarkUuids(userId);
	}
}
