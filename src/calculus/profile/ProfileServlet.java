package calculus.profile;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ProfileServlet extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
		
		String profileRequested = req.getRequestURI();
		profileRequested = profileRequested.substring(profileRequested.indexOf("/user/") + 6);
		
		
		boolean sameUser;
		User viewer = UserServiceFactory.getUserService().getCurrentUser();
		if (viewer == null){
			sameUser = false;
		} else {
			sameUser = profileRequested.equals(viewer.getUserId());
		}
	
		if (sameUser){
			UserContextAPI.addPersonalProfileContextToRequest(req);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/private-profile.jsp");
			jsp.forward(req, resp);
		} else {
			UserContextAPI.addPublicProfileInformationToRequest(req, profileRequested);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/public-profile.jsp");
			jsp.forward(req, resp);
		}
	}
}
