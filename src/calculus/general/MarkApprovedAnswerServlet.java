package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.api.KarmaAPI;
import calculus.models.Answer;
import calculus.utilities.UuidTools;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class MarkApprovedAnswerServlet extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) return;
		
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		Answer answer = new Answer(uuid);
		
		String parentUuid = answer.getParentUuid();
		String parentAuthorUserId = ContentAPI.getContentAuthorId(parentUuid);
		if (!parentAuthorUserId.equals(user.getUserId())) return;
		
		if (req.getRequestURI().contains("/not/")){
			answer.markNotApproved();
			KarmaAPI.incrementUserKarmaFromApprovedAnswers(answer.getAuthor().getUserId(), -10);
		} else {
			answer.markApproved();
			KarmaAPI.incrementUserKarmaFromApprovedAnswers(answer.getAuthor().getUserId(), 10);
		}
		
		String redirectUrl = "/content/" + answer.getParentUuid();
		
		resp.sendRedirect(redirectUrl);
	}
}
