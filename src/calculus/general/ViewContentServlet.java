package calculus.general;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.TextContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.recommendation.InterestsAPI;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ViewContentServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
			
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		if (uuid == null || uuid.length() != 36){
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		} 
			
		String contentType;
		try {
			contentType = ContentAPI.getContentType(uuid);
			User user = UserServiceFactory.getUserService().getCurrentUser();
			if (user != null){
				InterestsAPI.userViewedContent(user.getUserId(), uuid);
			}
			
		} catch (EntityNotFoundException e) {
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		if (contentType.equals("practiceProblem")) {
			PracticeProblem pp;
			try {
				pp = new PracticeProblem(uuid);
			} catch (EntityNotFoundException e) {
				// We should never hit this piece of code, as we just checked that the Content existed (above) and is the correct type.
				// The only case we could have with an error here is if we happen to be looking for it at the precise moment it is 
				// deleted.  This is unlikely enough that we can simply return a page-not-found.
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
				jsp.forward(req, resp);
				return;
			}
			resp.setContentType("text/html");
			RequestDispatcher jsp;
			if (pp.getSubmitted() && pp.getViewable()){
				PracticeProblemAPI.addPracticeProblemContext(req, pp);
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");
			} else if (!UserServiceFactory.getUserService().isUserLoggedIn()){
				jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			} else if (UserServiceFactory.getUserService().isUserAdmin()) {
				PracticeProblemAPI.addPracticeProblemContext(req, pp);
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");
			} else {	
				jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			}
			jsp.forward(req, resp);
		} else if (contentType.equals("question")) {
			Question q;
			try {
				q = new Question(uuid);
			} catch (EntityNotFoundException e) {
				// We should never hit this piece of code, as we just checked that the Content existed (above) and is the correct type.
				// The only case we could have with an error here is if we happen to be looking for it at the precise moment it is 
				// deleted.  This is unlikely enough that we can simply return a page-not-found.
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
				jsp.forward(req, resp);
				return;
			}
			resp.setContentType("text/html");
			RequestDispatcher jsp;
			if (q.getSubmitted() && q.getViewable()){
				QuestionAPI.addQuestionContext(req, q);
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");
			} else if (!UserServiceFactory.getUserService().isUserLoggedIn()){
				jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			} else if (UserServiceFactory.getUserService().isUserAdmin()) {
				QuestionAPI.addQuestionContext(req, q);
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");
			} else {
				jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			}
			jsp.forward(req, resp);
		} else if (contentType.equals("textContent")) {
			TextContent tc;
			try {
				tc = new TextContent(uuid);
			} catch (EntityNotFoundException e) {
				// We should never hit this piece of code, as we just checked that the Content existed (above) and is the correct type.
				// The only case we could have with an error here is if we happen to be looking for it at the precise moment it is 
				// deleted.  This is unlikely enough that we can simply return a page-not-found.
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
				jsp.forward(req, resp);
				return;
			}
			resp.setContentType("text/html");
			RequestDispatcher jsp;
			if (tc.getSubmitted() && tc.getViewable()){
				TextContentAPI.addTextContentContext(req, tc);
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/text-content.jsp");
			} else if (!UserServiceFactory.getUserService().isUserLoggedIn()){
				jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			} else if (UserServiceFactory.getUserService().isUserAdmin()) {
				TextContentAPI.addTextContentContext(req, tc);
				jsp = req.getRequestDispatcher("/WEB-INF/pages/content/text-content.jsp");
			} else {
				jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			}
			jsp.forward(req, resp);
		} else {	
			resp.getWriter().println("There is an issue. An unsupported content type <b>'"+ contentType +"'</b> was requested to be displayed.");
		}
	}
}
