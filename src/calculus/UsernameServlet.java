package calculus;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.UserInitializer;

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
		
		Entity userPublicInfo = UserInitializer.getOrCreateUserPublicInfo(user);
		
		String username = (String) req.getParameter("edit-username");
		
		UserInitializer.updateUserPublicInfo(user, "username", username);
		
		resp.sendRedirect((String) userPublicInfo.getProperty("profileUrl"));
	}	
}
