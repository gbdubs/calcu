package calculus.profile;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPublicInfoAPI;
import calculus.utilities.UrlGenerator;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ChangeUsernameServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		UserPublicInfoAPI.updateUsername(user, (String) req.getParameter("edit-username"));
		
		resp.sendRedirect(UrlGenerator.profileUrl(user));
	}	
}
