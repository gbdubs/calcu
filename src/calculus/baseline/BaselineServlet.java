package calculus.baseline;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

@SuppressWarnings("serial")
public class BaselineServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String urlRequest = req.getRequestURI();
		
		if (urlRequest.equals("/baseline")){
			// Baseline Landing
			UserContextAPI.addUserContextToRequest(req, "/baseline");
			
			req.setAttribute("beenHereBefore", false);
			
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/baseline-landing.jsp");	
			jsp.forward(req, resp);
			
		}
		
		
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		
		
		
		
		
	}
}
