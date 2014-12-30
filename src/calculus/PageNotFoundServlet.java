package calculus;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.UserContextAbstraction;

public class PageNotFoundServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAbstraction.addUserContextToRequest(req, "/page-not-found");
			
		resp.setContentType("text/html");
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		jsp.forward(req, resp);
	}
	
}
