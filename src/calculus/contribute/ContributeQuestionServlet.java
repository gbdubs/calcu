package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.QuestionAPI;
import calculus.api.UserContextAPI;
import calculus.models.Question;
import calculus.utilities.UuidTools;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContributeQuestionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/question/new");
			req.setAttribute("pageName", "Question Creation");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/question") +  9);
		
		if (urlRequest.startsWith("/edit/")){
			
			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			
			if (uuid != null && uuid.length() == 36){
				Question q = new Question(uuid);
				
				if (q.getSubmitted()){
					resp.sendRedirect("/practice-problem/"+uuid);
					return;
				} else {
				
					QuestionAPI.addQuestionContext(req, q);
					
					resp.setContentType("text/html");
					UserContextAPI.addUserContextToRequest(req, "/contribute/question/edit/" + uuid);
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/question.jsp");
					jsp.forward(req, resp);
					return;
				}
			}
		}
			
		UserContextAPI.addUserContextToRequest(req, "/contribute/question/new");
			
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/question.jsp");
		jsp.forward(req, resp);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		String uuid = QuestionAPI.createOrUpdateQuestionFromRequest(req);
		
		String saveButtonInstruction = req.getParameter("saveButton");
		
		if (saveButtonInstruction.equals("Save + Preview")){
			resp.sendRedirect("/contribute/preview/" + uuid);
			return;
		}
		if (saveButtonInstruction.equals("Save Changes")){
			resp.sendRedirect("/contribute/dashboard");
			return;
		} else {
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			RequestDispatcher jsp;
			req.setAttribute("readableContentType", "an interesting question");
			jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			jsp.forward(req, resp);
		}	
	}
}
