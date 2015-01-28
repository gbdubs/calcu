package calculus.contribute;

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

@SuppressWarnings("serial")
public class ContributePracticeProblemServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		// If the user is not logged in, redirect them to a page which describes that the page dictates a login
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			resp.setContentType("text/html");
			req.setAttribute("pageName", "Practice Problem Creation");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		String urlRequest = req.getRequestURI();
		// Finds the component of the URI which is specific to the request
		urlRequest = urlRequest.substring(urlRequest.indexOf("/practice-problem") +  17);
		
		// If we are in the editor, render the editor view
		if (urlRequest.startsWith("/edit/")){
			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			if (uuid != null && uuid.length() == 36){
				
				// TODO: Verify that the User is the Author
				
				// Create the practice problem from the UUID
				PracticeProblem pp = new PracticeProblem(uuid);
				// If they request editing a page which has already been submitted, redirect them to its
				// published state.
				if (pp.getSubmitted()){
					resp.sendRedirect("/practice-problem/" + uuid);
				} else {
					// Otherwise, display the problem for editing
					resp.setContentType("text/html");
					UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/edit/" + uuid);					
					PracticeProblemAPI.addPracticeProblemContext(req, pp);
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
					jsp.forward(req, resp);
				}
				return;
			}
		}
		
		// If for any reason we are not directed to a specific problem for editing, render a blank editor
		UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");	
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
		jsp.forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// TODO: Verify that the User is the Author
		
		// Save the practice problem that has been created or modified
		String uuid = PracticeProblemAPI.createOrUpdatePracticeProblemFromRequest(req);
		
		// This parameter describes which submit button was used, and tells us what mode the user wanted to use.
		String saveButtonInstruction = req.getParameter("saveButton");
		
		if (saveButtonInstruction.equals("Save + Preview")){
			// If we are saving and previewing, we have already saved, so now preview.
			resp.sendRedirect("/contribute/preview/" + uuid);
		} else if (saveButtonInstruction.equals("Save Changes")){
			// Simply reroute back to the dashboard.
			resp.sendRedirect("/contribute/dashboard");
		} else {
			// Otherwise, we have submitted, so we redirect to a thank you page.
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			req.setAttribute("readableContentType", "a practice problem");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			jsp.forward(req, resp);
		}	
	}
}
