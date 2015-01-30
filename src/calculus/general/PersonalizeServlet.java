package calculus.general;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.BookmarksAPI;
import calculus.api.RecommendationsAPI;
import calculus.api.UserContextAPI;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class PersonalizeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/personalize");
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String uri = req.getRequestURI();
		
		if (uri.equals("/personalize/landing") || user == null){
			req.setAttribute("loggedIn", user != null);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/landing.jsp");	
			jsp.forward(req, resp);
			return;
		}
		
		int stepNumber = getStepNumber(uri);
		
		if (stepNumber == -1){
			resp.sendRedirect("/personalize/landing");
			return;
		}
		
		req.setAttribute("stepNumber", stepNumber);
		
		if (stepNumber % 4 == 1){
			Map<String, Boolean> interests = RecommendationsAPI.getUserInterestPossibilities(user.getUserId());
			req.setAttribute("interests", interests);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/interests.jsp");	
			jsp.forward(req, resp);
			return;
		} else if (stepNumber % 3 == 0){
			
		} else {
			
		}
		
		
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String url = req.getRequestURI();
		
		if (url.contains("/personalize/interest")){
			String userId = req.getParameter("userId");
			String interest = req.getParameter("interest");
			String action = req.getParameter("action");
			
			if (action.equals("add")){
				RecommendationsAPI.addInterest(interest, userId);
			} else if (action.equals("remove")){
				RecommendationsAPI.removeInterest(interest, userId);
			}
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
