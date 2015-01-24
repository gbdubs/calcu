import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;

import calculus.api.UserContextAPI;
import calculus.models.Content;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class AnswerModeServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		String url = req.getRequestURI();
		String uuid = UuidTools.getUuidFromUrl(url);
		
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
			req.setAttribute("answerMode", true);
			req.setAttribute("answerModeStreak", 0);
			resp.setContentType("text/html");
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

	private void redirectToNewPracticeProblem(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}

	private void redirectToNewQuestion(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}

	private void questionLanding(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}

	private void practiceProblemLanding(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}

	private void pageNotFound(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}
}
