package calculus.profile;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.UserContextAPI;
import calculus.api.UserVerificationAPI;

@SuppressWarnings("serial")
public class ProfileServlet extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
		
		String profileRequested = req.getRequestURI();
		profileRequested = profileRequested.substring(profileRequested.indexOf("/user/") + 6);
		
		String viewerId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		boolean sameUser = profileRequested.equals(viewerId);
		
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
