package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.BookmarksAPI;

@SuppressWarnings("serial")
public class BookmarkServlet extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		String userId = req.getParameter("userId");
		String contentUuid = req.getParameter("contentUuid");
		String action = req.getParameter("action");
		
		System.out.println("ACTION: " + action);
		System.out.println("USERID: " + userId);
		System.out.println("CONTENT:" + contentUuid);
		
		if (action.equals("add")){
			BookmarksAPI.addBookmarkForUser(contentUuid, userId);
		} else {
			BookmarksAPI.deleteBookmarkForUser(contentUuid, userId);
		}
		
	}
}
