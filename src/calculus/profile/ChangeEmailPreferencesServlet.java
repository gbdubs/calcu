package calculus.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPrivateInfoAPI;
import calculus.utilities.UrlGenerator;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
 
@SuppressWarnings("serial")
public class ChangeEmailPreferencesServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) return;
		
		Map<String, String> preferences = new HashMap<String, String>();
		
		preferences.put("emailKarma", (String) req.getParameter("emailKarma"));
		preferences.put("emailRecommend", (String) req.getParameter("emailRecommend"));
		preferences.put("emailReply", (String) req.getParameter("emailReply"));
		
		UserPrivateInfoAPI.setUserEmailPreferences(user, preferences);
		
		resp.sendRedirect(UrlGenerator.profileUrl(user));
	}
}
