package calculus.contribute;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PracticeProblemAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.utilities.UuidTools;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class ContributeQuestionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			req.setAttribute("pageName", "Practice Problem Creation");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/practice-problem") +  17);
		
		if (urlRequest.startsWith("/edit/")){

			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			
			PracticeProblem pp = new PracticeProblem(uuid);
			PracticeProblemAPI.addPracticeProblemContext(req, pp);
			
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/edit/" + uuid);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
			jsp.forward(req, resp);
			
		} else {
			
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
				
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
			jsp.forward(req, resp);
			
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		PracticeProblemAPI.createOrUpdatePracticeProblemFromRequest(req);
		
		if (req.getAttribute("saveWork") == null){
			resp.sendRedirect("/contribute/dashboard");
			return;
		} else {
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			RequestDispatcher jsp;
			req.setAttribute("readableContentType", "a practice problem");
			jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			jsp.forward(req, resp);
		}	
	}
	
}
