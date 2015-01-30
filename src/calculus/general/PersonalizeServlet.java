package calculus.general;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;
import calculus.utilities.UuidTools;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class PersonalizeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/personalize");
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String uri = req.getRequestURI();
		
		if (uri.equals("/personalize/landing")){
			req.setAttribute("loggedIn", user != null);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize-landing.jsp");	
			jsp.forward(req, resp);
			return;
		}
		
		int stepNumber = getStepNumber(uri);
		
		if (stepNumber == -1){
			resp.sendRedirect("/personalize/landing");
			return;
		}
		
		
		
		
	}
	
	private static int getStepNumber(String uri){
		int location = uri.indexOf("/personalize/");
		if (location == -1) return -1;
		
		String remainder = uri.substring(location + 13);
		try{
			return Integer.parseInt(remainder);
		} catch (NumberFormatException e){
			return -1;
		}
	}
	
}
