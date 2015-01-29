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
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LivePreviewServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
			
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		if (uuid == null){
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		} 
			
		String contentType;
		try {
			contentType = ContentAPI.getContentType(uuid);
		} catch (EntityNotFoundException e) {
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		
		if (contentType.equals("practiceProblem")){
			PracticeProblem pp = new PracticeProblem(uuid);
			if (pp.getCreatorUserId().equals(userId) && !pp.getSubmitted()){
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");
				PracticeProblemAPI.addPracticeProblemContext(req, pp);	
				req.setAttribute("livePreview", true);
				jsp.forward(req, resp);
				return;
			}	
		} else if (contentType.equals("question") ){
			Question q = new Question(uuid);
			if (q.getCreatorUserId().equals(userId) && !q.getSubmitted()){
				resp.setContentType("text/html");
				QuestionAPI.addQuestionContext(req, q);
				req.setAttribute("livePreview", true);
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");	
				jsp.forward(req, resp);
				return;
			}
		} else {
			resp.getWriter().println("There is an issue. An unsupported content type <b>'"+ contentType +"'</b> was requested to be displayed.");
			return;
		}
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		jsp.forward(req, resp);
		return;
	}
}
