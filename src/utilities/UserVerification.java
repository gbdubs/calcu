package utilities;

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

public class UserVerification {

	public static boolean verifyUserProfileViewAccess(HttpServletRequest req){
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", user.getUserId());
		
		Entity publicInfo;
		try {
			publicInfo = ds.get(publicInfoKey);
		} catch (EntityNotFoundException e) {return false;}
		
		String userPublicId = (String) publicInfo.getProperty("publicId");
		
		
		String pageUrl = req.getRequestURI();
		int pageIdLocation = pageUrl.indexOf("/user/") + 6;
		if (pageIdLocation < 6) return false;
		
		String pagePublicId = pageUrl.substring(pageIdLocation);
		
		if (pagePublicId.equals(userPublicId)) return true;
		return false;
		
	}
	
}
