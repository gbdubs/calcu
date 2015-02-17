package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievementsAPI;
import calculus.api.AnswersAPI;
import calculus.api.ContentAPI;
import calculus.api.KarmaAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.UserContextAPI;
import calculus.models.Answer;
import calculus.recommendation.InterestsAPI;
import calculus.recommendation.SkillsAPI;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContributeAnswerServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		if (user == null){
			resp.sendRedirect("/login-required-to-post-answers");
		}
		
		// Creates and stores a new answer from the request
		Answer answer = AnswersAPI.createAnswerFromRequest(req);
		String uuid = answer.getUuid();
		
		// Save that the user is interested in this kind of content
		String userId = user.getUserId();
		String answerParentUuid = answer.getParentUuid();
		InterestsAPI.userAnsweredContent(userId, answerParentUuid);
		SkillsAPI.userAnsweredContent(userId, answerParentUuid);
		
		// Alert the parent that it has a new child!
		ContentAPI.addAnswerToContent(answerParentUuid, uuid);
		
		// Give the user their instant burst of Karma for answering the question
		KarmaAPI.incrementContentKarma(uuid, 3);
		
		// Records the answer for achievement purposes
		// Increment their stats for Achievments
		AchievementsAPI.incrementUserAchievementStatsFromContentSubmission(userId, req.getParameter("body"), "Answers");
		
		// Redirects the user to a thank you page
		resp.setContentType("text/html");
		UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
		req.setAttribute("readableContentType", "an answer");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
		jsp.forward(req, resp);
	}	
}
