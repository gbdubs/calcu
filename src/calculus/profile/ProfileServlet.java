package calculus.profile;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.UserContextAbstraction;
import utilities.UserVerification;

public class ProfileServlet extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAbstraction.addUserContextToRequest(req, req.getRequestURI());
		
		boolean permitted = UserVerification.verifyUserProfileViewAccess(req);
		
		if (!permitted){
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-access-denied.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		UserContextAbstraction.addProfileContextToRequest(req);
			
		resp.setContentType("text/html");
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/profile.jsp");
		jsp.forward(req, resp);
	}
	
}
