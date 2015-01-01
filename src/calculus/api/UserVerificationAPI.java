package calculus.api;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserVerificationAPI {

	public static boolean verifyUserProfileViewAccess(HttpServletRequest req){
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Entity userPublicInfo = UserDatastoreAPI.getOrCreateUserPublicInfo(user);
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
}
