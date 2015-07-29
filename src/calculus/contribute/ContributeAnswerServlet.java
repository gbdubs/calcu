package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievementsAPI;
import calculus.api.ContentAPI;
import calculus.api.KarmaAPI;
import calculus.api.NotificationsAPI;
import calculus.api.UserContextAPI;
import calculus.api.UserPublicInfoAPI;
import calculus.models.Answer;
import calculus.models.Notification;
import calculus.recommendation.InterestsAPI;
import calculus.recommendation.SkillsAPI;

import com.google.appengine.api.datastore.EntityNotFoundException;
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
		String uuid = ContentAPI.createOrUpdateContentFromRequest(req, "answer");
		Answer answer;
		try {
			answer = new Answer(uuid);
		} catch (EntityNotFoundException e) {
			// Return, there was error in the code that saves a piece of content.
			resp.getWriter().println("THERE WAS AN ERROR IN THE INSTANTIATION OF THE ANSWER: IT WAS NOT FOUND.");
			return;
		}
		
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
		
		// Sends the author a notification if the author is not null.
		String authorUserId = ContentAPI.getContentAuthorId(answerParentUuid);
		if (authorUserId != null && authorUserId.length() > 5){
			Notification n = ContributeAnswerServlet.answerNotification(answerParentUuid, userId);
			NotificationsAPI.sendNotification(n);
		}
		
		// Redirects the user to a thank you page
		resp.setContentType("text/html");
		UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
		req.setAttribute("readableContentType", "an answer");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
		jsp.forward(req, resp);
	}

	public static Notification answerNotification(String parentUuid, String userId){
		String verb = "answered";
		String readableContentType = "Content";	
		String urlContentType = "content";
		String contentType;
		try {
			contentType = ContentAPI.getContentType(parentUuid);
		} catch (EntityNotFoundException e) {
			contentType = "A Very Strange Error";
		}
		String authorId = ContentAPI.getContentAuthorId(parentUuid);
		if (contentType.equals("practiceProblem")){
			readableContentType = "Practice Problem";
			urlContentType = "practice-problem";
		}
		else if (contentType.equals("question")){
			readableContentType = "Question";
			urlContentType = "question";
		}
		else if (contentType.equals("textContent")){
			verb = "commented on";
			readableContentType = "Explanation";
			urlContentType = "text-content";
		} else {
			readableContentType = contentType;
		}
		
		String notificationBody = UserPublicInfoAPI.getUsername(userId) + " " + verb + " your " + readableContentType;
		Notification n = new Notification()
			.withRecipientId(authorId)
			.withAssociatedUserId(userId)
			.withTime(System.currentTimeMillis())
			.withTitle("New Answer")
			.withBody(notificationBody)
			.withImageUrl(UserPublicInfoAPI.getProfilePictureUrl(userId))
			.withUrl("/"+urlContentType+"/"+parentUuid)
			.withColor("danger");
		return n;
	}	
}
