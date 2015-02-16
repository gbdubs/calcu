package calculus.general;

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
public class RecommendationsServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();

		resp.setContentType("text/html");
		String requestUri = req.getRequestURI();
		UserContextAPI.addUserContextToRequest(req, requestUri);
		
		RequestDispatcher jsp;
		if (user == null){
			jsp = req.getRequestDispatcher("/WEB-INF/pages/recommendations-logged-out.jsp");	
		} else {
			jsp = req.getRequestDispatcher("/WEB-INF/pages/recommendations.jsp");	
		}
		jsp.forward(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
	}
}
