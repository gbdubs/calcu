package calculus.general;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AnswersAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.UserContextAPI;
import calculus.api.UserPrivateInfoAPI;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AnswerModeServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		String url = req.getRequestURI();
		String uuid = UuidTools.getUuidFromUrl(url);
		
		UserContextAPI.addUserContextToRequest(req, "/answer");
		setAnswerModeValues(req);
		resp.setContentType("text/html");
		
		if (uuid == null || uuid.length() != 36){
			if (url.contains("new")){
				if (url.contains("question")){
					redirectToNewQuestion(req, resp); return;
				} else if (url.contains("practice-problem")){
					redirectToNewPracticeProblem(req, resp); return;
				} else {
					pageNotFound(req, resp); return;
				}
			} else {
				if (url.contains("question")){
					questionLanding(req, resp); return;
				} else if (url.contains("practice-problem")){
					practiceProblemLanding(req, resp); return;
				}
			}
		} else {
			String contentType;
			try {
				contentType = Content.getContentType(uuid);
			} catch (EntityNotFoundException e) {
				pageNotFound(req, resp); return;
			}
			String newUrl = "/answer/" + contentType + "/" + uuid;
			RequestDispatcher jsp;
			if (contentType.equals("question")){
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");	
			} else if (contentType.equals("practice-problem")){
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");
			} else {
				pageNotFound(req, resp); return;
			}
			jsp.forward(req, resp);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		String url = req.getRequestURI();
		String uuid = UuidTools.getUuidFromUrl(url);
		String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		
		int streak = Integer.parseInt((String) req.getParameter("answerModeStreak"));
		
		if (url.contains("skip")){
			UserPrivateInfoAPI.addUserSkippedContent(userId, uuid);
		} else if (url.contains("done")){
			UserPrivateInfoAPI.addUserAnsweredContent(userId, uuid);
		}
	}

	private void redirectToNewPracticeProblem(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		PracticeProblem pp = PracticeProblemAPI.getAnswerablePracticeProblem(user.getUserId());
		req.setAttribute("practiceProblem", pp);
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");
		jsp.forward(req, resp);
	}

	private void redirectToNewQuestion(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		Question q = QuestionAPI.getAnswerableQuestion(user.getUserId());
		req.setAttribute("question", q);
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");
		jsp.forward(req, resp);
	}

	private void questionLanding(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/answer/question-landing.jsp");
		jsp.forward(req, resp);
	}

	private void practiceProblemLanding(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/answer/practice-problem-landing.jsp");
		jsp.forward(req, resp);
	}

	private void pageNotFound(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect("page-not-found");
	}
	
	private void setAnswerModeValues(HttpServletRequest req){
		String url = req.getRequestURI();
		String uuid = UuidTools.getUuidFromUrl(url);
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		int streak = 0;
		String streakString = (String) req.getParameter("answerModeStreak");
		if (streakString != null){
			Integer i = Integer.parseInt(streakString);
			if (i != null){
				streak = i;
				if (url.contains("success")){
					boolean success = AnswersAPI.userAnsweredContent(user.getUserId(), uuid);
					if (success){
						streak++;
					} else {
						streak = 0;
					}
				}
			} else {
				streak = 0;
			}
		}
		req.setAttribute("answerModeStreak", streak);
		req.setAttribute("answerMode", true);
	}
}
