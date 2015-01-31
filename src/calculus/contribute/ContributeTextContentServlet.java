package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.TextContentAPI;
import calculus.api.UserContextAPI;
import calculus.api.UserVerificationAPI;
import calculus.models.TextContent;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class ContributeTextContentServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		// Verifies that there is a user for this
		if (!UserVerificationAPI.verifyUserLoggedIn("/contribute/text-content/new", "Contribute Text Content Page", req, resp));

		// Finds the request that directs us to the user's aims.
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/text-content") +  13);
		
		// If they are seeking out to edit a specific problem
		if (urlRequest.startsWith("/edit/")){
			
			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			
			if (uuid != null && uuid.length() == 36){
				
				// TODO: Verify that the User is the Author
				
				TextContent tc = new TextContent(uuid);
				
				//If the TextContent is already submitted, redirect the user to the live page with it.
				if (tc.getSubmitted()){	
					resp.sendRedirect("/practice-problem/"+uuid);
				} else {
					// Adds the current TextContent to the context, and prepares it for editing.
					TextContentAPI.addTextContentContext(req, tc);
					resp.setContentType("text/html");
					UserContextAPI.addUserContextToRequest(req, "/contribute/text-content/edit/" + uuid);
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/text-content.jsp");
					jsp.forward(req, resp);
				}
				return;
			}
		}
		
		// If any section of the above fails, we don't have a specific problem to edit, so edit a blank one
		UserContextAPI.addUserContextToRequest(req, "/contribute/text-content/new");
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/text-content.jsp");
		jsp.forward(req, resp);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		// TODO: Verify that the User is the Author
		
		// Saves the new/updated TextContent, and now we have to decide what to next show the user.
		String uuid = TextContentAPI.createOrUpdateTextContentFromRequest(req);
		
		String saveButtonInstruction = req.getParameter("saveButton");
		
		if (saveButtonInstruction.equals("Save + Preview")){
			// Redirects the user to see a non-live version of their content
			resp.sendRedirect("/contribute/preview/" + uuid);
		} else if (saveButtonInstruction.equals("Save Changes")){
			// Redirects the user to see their past created content
			resp.sendRedirect("/contribute/dashboard");
		} else {
			// Otherwise we have submitted, so we thank the user for their contribution
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			req.setAttribute("readableContentType", "some interesting content");
			jsp.forward(req, resp);
		}	
	}
}
