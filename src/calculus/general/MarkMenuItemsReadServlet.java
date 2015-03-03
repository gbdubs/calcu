package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.BookmarksAPI;
import calculus.api.NotificationsAPI;
import calculus.recommendation.PublicRecommendationAPI;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class MarkMenuItemsReadServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String userId = req.getParameter("userId");
		String type = req.getParameter("type");
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (type == null || user == null || !user.getUserId().equals(userId)) return;
		
		if (type.equals("bookmarks")) {
			System.out.println("Clearing Bookmarks...");
			BookmarksAPI.markAllBookmarksRead(userId);
		} else if (type.equals("notifications")) {
			System.out.println("Clearing Notifications...");
			NotificationsAPI.markAllNotificationsRead(userId);
		} else if (type.equals("recommendations")) {
			System.out.println("Clearing Recommendations...");
			PublicRecommendationAPI.markAllRecommendationsRead(userId);
		} else {
			System.out.println("Unrecognized Type Argument: " + type);
		}
	}
}