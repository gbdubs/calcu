package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;
import calculus.models.Answer;

@SuppressWarnings("serial")
public class ContributeAnswerServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Creates and stores a new answer from the request
		Answer.createAnswerFromRequest(req);
		
		// Redirects the user to a thank you page
		resp.setContentType("text/html");
		UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
		req.setAttribute("readableContentType", "an answer");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
		jsp.forward(req, resp);
	}	
}
