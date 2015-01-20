package calculus.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPrivateInfoAPI;
import calculus.api.UserVerificationAPI;
import calculus.utilities.UrlGenerator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
 
@SuppressWarnings("serial")
public class ChangeEmailPreferencesServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		Map<String, String> preferences = new HashMap<String, String>();
		
		preferences.put("emailKarma", (String) req.getParameter("emailKarma"));
		preferences.put("emailRecommend", (String) req.getParameter("emailRecommend"));
		preferences.put("emailReply", (String) req.getParameter("emailReply"));
		
		UserPrivateInfoAPI.setUserEmailPreferences(user, preferences);
		
		resp.sendRedirect(UrlGenerator.profileUrl(user));
	}
}
