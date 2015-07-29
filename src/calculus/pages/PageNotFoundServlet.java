package calculus.pages;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

@SuppressWarnings("serial")
public class PageNotFoundServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
		
		String requestUri = req.getRequestURI();
		if (requestUri.equals("") || requestUri.equals("/")){
			resp.sendRedirect("/home");
			return;
		}
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		jsp.forward(req, resp);
	}
}
