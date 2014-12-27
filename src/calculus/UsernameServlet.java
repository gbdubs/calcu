package calculus;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UsernameServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Key userPublicInfoKey = KeyFactory.createKey("UserPublicInfo", user.getUserId());
		
		Entity userPublicInfo = new Entity(userPublicInfoKey);
		try {
			userPublicInfo = ds.get(userPublicInfoKey);
		} catch (EntityNotFoundException e) {
			userPublicInfo.setProperty("karma", 0);
		}
		
		String username = (String) req.getParameter("username");
		userPublicInfo.setProperty("username", username);
		
		ds.put(userPublicInfo);
		
		resp.sendRedirect("/profile");
	}
	
}
