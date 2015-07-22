package calculus.profile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPublicInfoAPI;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AffirmConsentServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String userId = req.getParameter("userId");
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null || !user.getUserId().equals(userId)) return;
		
		UserPublicInfoAPI.affirmConsent(userId);
	}
}
