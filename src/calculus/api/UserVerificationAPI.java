package calculus.api;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserVerificationAPI {

	private static UserService userService = UserServiceFactory.getUserService();
	
	public static boolean verifyUserProfileViewAccess(HttpServletRequest req){
		
		User user = userService.getCurrentUser();
		
		Entity userPublicInfo = UserPublicInfoAPI.getOrCreateMyPublicInfo(user);
		if (userPublicInfo == null) return false;
		String userId = (String) userPublicInfo.getProperty("userId");
		
		String pageUrl = req.getRequestURI();
		int pageIdLocation = pageUrl.indexOf("/user/") + 6;
		if (pageIdLocation < 6) return false;
		
		// Since User IDs are only 20 characters long, we only check the 20 after the '/user/'
		// Thus we have to reject any strings shorter than this length.
		if (pageUrl.length() < 26) return false;
		String pagePublicId = pageUrl.substring(pageIdLocation, pageIdLocation + 20);
		
		if (pagePublicId.equals(userId)) return true;
		System.out.println("USER: '"+  userId + "' attempted to access page '"+ pagePublicId +"'.  The attempt was blocked.");
		
		return false;
	}
	
	public static boolean verifyUserLoggedIn(String currentUrl, String pageName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		User user = userService.getCurrentUser();
		
		// If we don't have a user logged in, remind them that they must be logged in in order to access the
		// page they have sought out.
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, currentUrl);
			req.setAttribute("pageName", pageName);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return false;
		}
		
		return true;
	}
}
