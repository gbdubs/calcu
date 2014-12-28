package calculus;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
public class TagTesterServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String loginUrl = userService.createLoginURL("/tag-tester");
		String logoutUrl = userService.createLogoutURL("/tag-tester");
		
		req.setAttribute("user", user);
		req.setAttribute("loginUrl", loginUrl);
		req.setAttribute("logoutUrl", logoutUrl);

		if (user != null){
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", user.getUserId());
			int karma; String username, profilePictureUrl;
			try {
				Entity publicInfo = ds.get(publicInfoKey);
				karma = ((Long) publicInfo.getProperty("karma")).intValue();
				username = (String) publicInfo.getProperty("username");
				profilePictureUrl = (String) publicInfo.getProperty("profilePictureUrl");
			} catch (EntityNotFoundException e) {
				karma = 0;
				username = user.getEmail();
				profilePictureUrl = "/_static/img/default-avatar.png";
			}
			req.setAttribute("username", username);
			req.setAttribute("karma", KarmaDescription.toMediumString(karma)); 
			req.setAttribute("profilePictureUrl", profilePictureUrl);
			req.setAttribute("email", user.getEmail());
		} else {
			req.setAttribute("email", "anonymous314159@gmail.com");
			req.setAttribute("username", "Anonymous Elephant");
			req.setAttribute("karma", "None");
			req.setAttribute("profilePictureUrl", "/_static/img/elephant.png");
		}
		
		req.setAttribute("profileUrl", "/");
		
		resp.setContentType("text/html");
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/successpage.jsp");
		jsp.forward(req, resp);
	}
}
