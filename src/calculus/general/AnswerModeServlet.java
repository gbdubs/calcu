package calculus.general;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.UserContextAPI;
import calculus.api.UserPrivateInfoAPI;
import calculus.models.Answer;
import calculus.models.PracticeProblem;
import calculus.models.Question;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AnswerModeServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		if (UserServiceFactory.getUserService().getCurrentUser() == null){
			resp.sendRedirect("/page-not-found"); return;
		}
		
		redirectToNewProblemOrDashboard(req, resp, 0);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String uuid = (String) req.getParameter("parentUuid");
		String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		int streak = Integer.parseInt((String) req.getParameter("answerModeStreak"));
		if (req.getParameter("action").equals("skip")){
			UserPrivateInfoAPI.addUserSkippedContent(userId, uuid);
		} else if (req.getParameter("action").equals("done")){
			Answer.createAnswerFromRequest(req);
			UserPrivateInfoAPI.addUserAnsweredContent(userId, uuid);
			streak++;
		}
		redirectToNewProblemOrDashboard(req, resp, streak);
	}
	
	private void redirectToNewProblemOrDashboard(HttpServletRequest req, HttpServletResponse resp, int streak) throws ServletException, IOException{
		String url = req.getRequestURI();

		req.setAttribute("answerModeStreak", streak);
		req.setAttribute("answerMode", true);
		
		UserContextAPI.addUserContextToRequest(req, "/answer");
		resp.setContentType("text/html");
		
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
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/answer-landing.jsp");
		jsp.forward(req, resp);
	}

	private void practiceProblemLanding(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/answer-landing.jsp");
		jsp.forward(req, resp);
	}

	private void pageNotFound(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect("page-not-found");
	}
}
