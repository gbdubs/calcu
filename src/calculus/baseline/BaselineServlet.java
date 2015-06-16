package calculus.baseline;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.BaselineAPI;
import calculus.api.UserContextAPI;
import calculus.models.Question;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class BaselineServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		boolean userLoggedIn = UserServiceFactory.getUserService().getCurrentUser() != null;
		
		UserContextAPI.addUserContextToRequest(req, "/baseline");
		
		
		String urlRequest = req.getRequestURI();
		// Baseline Landing
		if (urlRequest.equals("/baseline")){	
			req.setAttribute("beenHereBefore", false);
			req.setAttribute("userLoggedIn", userLoggedIn);
			
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/baseline-landing.jsp");	
			jsp.forward(req, resp);
			return;
		}
		
		if (urlRequest.equals("/baseline/congrats")){
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/baseline-done.jsp");	
			jsp.forward(req, resp);
			return;
		}

		int start = urlRequest.indexOf("/", 1);
		int end = urlRequest.indexOf("/", start+1);
		if (end == -1){
			end = urlRequest.length();
		}
		String result = urlRequest.substring(start+1, end);
		
		int stepNumber = Integer.parseInt(result);
		
		String uuid = UuidTools.getUuidFromUrl(urlRequest);
		
		if (uuid == null){
			String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
			uuid = BaselineAPI.getQuestionForBaseliningUser(userId);
			resp.sendRedirect("/baseline/" + stepNumber + "/" + uuid);
			return;
		} else if (stepNumber < 11){
			Question q = new Question(uuid);
			req.setAttribute("question", q);
			req.setAttribute("stepNumber", stepNumber);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/baseline-step.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.sendRedirect("/baseline/congrats");
		}
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		String problemUuid = req.getParameter("problemUuid");
		int stepNumber = Integer.parseInt(req.getParameter("stepNumber"));
		int difficulty = 0;
		if (req.getParameter("diff1") != null){
			difficulty = 1;
		} else if (req.getParameter("diff2") != null){
			difficulty = 2;
		} else if (req.getParameter("diff3") != null){
			difficulty = 3;
		} else if (req.getParameter("diff4") != null){
			difficulty = 4;
		} else if (req.getParameter("diff5") != null){
			difficulty = 5;
		}
		BaselineAPI.userRankedProblemWithDifficulty(userId, problemUuid, difficulty);
		
		resp.sendRedirect("/baseline/" + (stepNumber+1));
	}
}
