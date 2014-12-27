package calculus;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
 
@SuppressWarnings("serial")
public class EmailPreferencesServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
		Key emailPrefsKey = KeyFactory.createKey("UserEmailPrefs", user.getUserId());
		Entity emailPrefs = new Entity(emailPrefsKey);
		
		emailPrefs.setProperty("replyPref", (String) req.getParameter("reply-pref"));
		emailPrefs.setProperty("recommendPref", (String) req.getParameter("recommend-pref"));
		emailPrefs.setProperty("karmaPref", (String) req.getParameter("karma-pref"));
		
		ds.put(emailPrefs);
		
		resp.sendRedirect("/profile");
	}

}
