package calculus.contribute;

import java.util.List;
import java.io.IOException;

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

public class ContributePracticeProblemServlet extends HttpServlet {
	
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
			if (uuid != null && uuid.length() == 36){
				PracticeProblem pp = new PracticeProblem(uuid);
				
				if (pp.getSubmitted()){
					resp.sendRedirect("/practice-problem/"+uuid);
					return;
				} else {
					PracticeProblemAPI.addPracticeProblemContext(req, pp);
					resp.setContentType("text/html");
					UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/edit/" + uuid);
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
					jsp.forward(req, resp);
					return;
				}
			}
		} 
			
		UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
		jsp.forward(req, resp);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		String uuid = PracticeProblemAPI.createOrUpdatePracticeProblemFromRequest(req);
		
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
			req.setAttribute("readableContentType", "a practice problem");
			jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			jsp.forward(req, resp);
		}	
	}
}
