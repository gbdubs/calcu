package calculus.general;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.BaselineAPI;
import calculus.api.ContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserServiceFactory;

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
		
		int stepNumber;
		try {
			stepNumber = Integer.parseInt(result);
		} catch (java.lang.NumberFormatException jnfe){
			resp.sendRedirect("/baseline");
			return;
		}
		
		String uuid = UuidTools.getUuidFromUrl(urlRequest);
		
		if (uuid == null){
			int currentDifficulty = 201000;
			if (end != -1){
				try {
					currentDifficulty = Integer.parseInt(urlRequest.substring(end+1));
				} catch (java.lang.NumberFormatException jnfe){
					
				} catch (java.lang.StringIndexOutOfBoundsException sioobe){
					currentDifficulty = 201000;
				}
			}
			req.setAttribute("difficulty", currentDifficulty);
			uuid = BaselineAPI.getQuestionForBaseliningUser(currentDifficulty);
		}
		if (uuid != null && stepNumber < 11){
			Content c;
			try {
				c = ContentAPI.instantiateContent(uuid);
			} catch (EntityNotFoundException e) {
				// If the requested piece of content does not exist, 
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
				jsp.forward(req, resp);
				return;
			}
			req.setAttribute("content", c);
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
		int problemDifficulty = Integer.parseInt(req.getParameter("difficulty"));
		
		double difficulty = 0;
		if (req.getParameter("diff1") != null){
			difficulty = .2;
		} else if (req.getParameter("diff2") != null){
			difficulty = .4;
		} else if (req.getParameter("diff3") != null){
			difficulty = .6;
		} else if (req.getParameter("diff4") != null){
			difficulty = .8;
		} else if (req.getParameter("diff5") != null){
			difficulty = 1;
		}
		BaselineAPI.userRankedProblemWithDifficulty(userId, problemUuid, difficulty);
		
		int nextDifficulty = BaselineAPI.transformDifficulty(problemDifficulty, (int) (difficulty * 10), stepNumber);
		
		resp.sendRedirect("/baseline/" + (stepNumber+1) + "/" + nextDifficulty);
	}
}
