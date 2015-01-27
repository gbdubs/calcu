package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.models.Answer;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class MarkApprovedAnswerServlet extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		Answer answer = new Answer(uuid);
		
		if (req.getRequestURI().contains("/not/")){
			answer.markNotApproved();
		} else {
			answer.markApproved();
		}
		
		String redirectUrl = "/content/" + answer.getParentUuid();
		
		resp.sendRedirect(redirectUrl);
	}
}
