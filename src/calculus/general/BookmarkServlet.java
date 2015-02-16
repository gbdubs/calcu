package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.BookmarksAPI;
import calculus.recommendation.InterestsAPI;

@SuppressWarnings("serial")
public class BookmarkServlet extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String userId = req.getParameter("userId");
		String contentUuid = req.getParameter("contentUuid");
		String action = req.getParameter("action");
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null || !user.getUserId().equals(userId)) return;
		
		if (action.equals("add")){
			BookmarksAPI.addBookmarkForUser(contentUuid, userId);
			InterestsAPI.userBookmarkedContent(userId, contentUuid);
		} else if (action.equals("remove")){
			BookmarksAPI.deleteBookmarkForUser(contentUuid, userId);
		}
	}
}