package calculus.contribute;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class ContributeDashboardServlet extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			req.setAttribute("pageName", "Content Creation Dashboard");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		List<PracticeProblem> unsubmittedPP = PracticeProblemAPI.getUnsubmittedPracticeProblems(user);
		List<PracticeProblem> submittedPP = PracticeProblemAPI.getSubmittedPracticeProblems(user);
		List<Question> unsubmittedQ = QuestionAPI.getUnsubmittedQuestions(user);
		List<Question> submittedQ = QuestionAPI.getSubmittedQuestions(user);
		// List<TextContent> unsubmittedC = TextContentAPI.getUnsubmittedQuestions(user);
		// List<TextContent> submittedC = TextContentAPI.getSubmittedQuestions(user);
	
		UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
		req.setAttribute("unsubmittedPracticeProblems", unsubmittedPP);
		req.setAttribute("submittedPracticeProblems", submittedPP);
		req.setAttribute("unsubmittedQuestions", unsubmittedQ);
		req.setAttribute("submittedQuestions", submittedQ);
		// req.setAttribute("unsubmittedContent", unsubmittedC);
		// req.setAttribute("submittedContent", submittedC);
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/dashboard.jsp");
		jsp.forward(req, resp);
	}
	
}
