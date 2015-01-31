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
import calculus.api.TextContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContributeDashboardServlet extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		// If there is no user, do not display the content dashboard.
		if (user == null) {
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			resp.setContentType("text/html");
			// Tells the user they should login to access this page
			req.setAttribute("pageName", "Content Creation Dashboard");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		String userId = user.getUserId();
		
		// Retrieves the Problems and Questions that the user has created
		List<PracticeProblem> unsubmittedPP = PracticeProblemAPI.getUnsubmittedPracticeProblems(userId);
		List<PracticeProblem> submittedPP = PracticeProblemAPI.getSubmittedPracticeProblems(userId);
		List<Question> unsubmittedQ = QuestionAPI.getUnsubmittedQuestions(userId);
		List<Question> submittedQ = QuestionAPI.getSubmittedQuestions(userId);
		List<TextContent> unsubmittedTC = TextContentAPI.getUnsubmittedTextContent(userId);
		List<TextContent> submittedTC = TextContentAPI.getSubmittedTextContent(userId);
	
		req.setAttribute("unsubmittedPracticeProblems", unsubmittedPP);
		req.setAttribute("submittedPracticeProblems", submittedPP);
		req.setAttribute("unsubmittedQuestions", unsubmittedQ);
		req.setAttribute("submittedQuestions", submittedQ);
		req.setAttribute("unsubmittedContent", unsubmittedTC);
		req.setAttribute("submittedContent", submittedTC);
		
		UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/dashboard.jsp");
		jsp.forward(req, resp);
	}
}
