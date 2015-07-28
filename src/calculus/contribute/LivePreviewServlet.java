package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LivePreviewServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null){
			sendPageNotFound(req, resp);
			return;
		}
		String userId = user.getUserId();
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
			
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		if (uuid == null || uuid.length() != 36){
			sendPageNotFound(req, resp);
			return;
		}
		
		String authorId = ContentAPI.getContentAuthorId(uuid);
		if (!authorId.equals(userId)){
			resp.sendRedirect("/page-not-found");
		}
		
		
		String contentType;
		try {
			contentType = ContentAPI.getContentType(uuid);
		} catch (EntityNotFoundException e) {
			sendPageNotFound(req, resp);
			return;
		}
		
		
		if (contentType.equals("practiceProblem")){
			PracticeProblem pp;
			try {
				pp = new PracticeProblem(uuid);
			} catch (EntityNotFoundException e) {
				sendPageNotFound(req, resp);
				return;
			}
			if (pp.getCreatorUserId().equals(userId) && !pp.getSubmitted()){
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");
				req.setAttribute("practiceProblem", pp);
				req.setAttribute("livePreview", true);
				jsp.forward(req, resp);
				return;
			}	
		} else if (contentType.equals("question") ){
			Question q;
			try {
				q = new Question(uuid);
			} catch (EntityNotFoundException e) {
				sendPageNotFound(req, resp);
				return;
			}
			if (q.getCreatorUserId().equals(userId) && !q.getSubmitted()){
				resp.setContentType("text/html");
				req.setAttribute("question", q);
				req.setAttribute("livePreview", true);
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");	
				jsp.forward(req, resp);
				return;
			}
		} else if (contentType.equals("textContent")) {
			TextContent tc;
			try {
				tc = new TextContent(uuid);
			} catch (EntityNotFoundException e) {
				sendPageNotFound(req, resp);
				return;
			}
			if (tc.getCreatorUserId().equals(userId) && !tc.getSubmitted()){
				resp.setContentType("text/html");
				req.setAttribute("textContent", tc);
				req.setAttribute("livePreview", true);
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/text-content.jsp");	
				jsp.forward(req, resp);
				return;
			}
		} else {
			resp.getWriter().println("There is an issue. An unsupported content type <b>'"+ contentType +"'</b> was requested to be displayed.");
			return;
		}
		
		sendPageNotFound(req, resp);
	}
	
	private static void sendPageNotFound(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		jsp.forward(req, resp);
	}
}
