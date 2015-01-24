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

	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		Answer answer = Answer.createAnswerFromRequest(req);
		resp.setContentType("text/html");
		if (req.getParameter("answerMode") != null){
			String nextUrl = req.getParameter("answerModeNextUrl");
			resp.sendRedirect(nextUrl);
		} else {
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			RequestDispatcher jsp;
			jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/answer-thanks.jsp");
			jsp.forward(req, resp);
		}
	}	
}
