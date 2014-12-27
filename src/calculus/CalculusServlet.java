package calculus;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

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
public class CalculusServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String loginUrl = userService.createLoginURL("/profile");
		String logoutUrl = userService.createLogoutURL("/profile");
		
		if (user == null){
			// If the user is not logged in, we only need provide them with
			// the information that enables them to login.
			req.setAttribute("user", null);
			req.setAttribute("loginUrl", loginUrl);
			
		} else {
			req.setAttribute("user", user);
			req.setAttribute("loginUrl", loginUrl);
			req.setAttribute("logoutUrl", logoutUrl);
			
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			
			Key prefsKey = KeyFactory.createKey("UserEmailPrefs", user.getUserId());
			
			// Sets preferences, or defaults to none.
			String replyPref, recommendPref, karmaPref;
			try {
				Entity emailPrefs = ds.get(prefsKey);
				replyPref = (String) emailPrefs.getProperty("replyPref");
				recommendPref = (String) emailPrefs.getProperty("recommendPref");
				karmaPref = (String) emailPrefs.getProperty("karmaPref");
			} catch (EntityNotFoundException e) {
				replyPref = "none";
				recommendPref = "none";
				karmaPref = "none";
			}
			req.setAttribute("replyPref", replyPref);
			req.setAttribute("recommendPref", recommendPref);
			req.setAttribute("karmaPref", karmaPref);
			
			// Sets Public information values (Username and Karma)
			Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", user.getUserId());
			int karma;
			String username;
			try {
				Entity publicInfo = ds.get(publicInfoKey);
				karma = ((Long) publicInfo.getProperty("karma")).intValue();
				username = (String) publicInfo.getProperty("username");
			} catch (EntityNotFoundException e) {
				karma = 0;
				username = user.getEmail();
			}
			req.setAttribute("username", username);
			req.setAttribute("karma", KarmaDescription.toMediumString(karma));
		}
		
		resp.setContentType("text/html");
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/profile.jsp");
		jsp.forward(req, resp);
	}
}
