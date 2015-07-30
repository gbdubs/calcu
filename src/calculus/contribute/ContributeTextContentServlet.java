package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievementsAPI;
import calculus.api.ContentAPI;
import calculus.api.KarmaAPI;
import calculus.api.UserContextAPI;
import calculus.models.TextContent;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContributeTextContentServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		// If the user is not logged in, redirect them to a page which describes that the page dictates a login
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/text-content/new");
			resp.setContentType("text/html");
			req.setAttribute("pageName", "Explanation Creation");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		// Finds the request that directs us to the user's aims.
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/text-content") +  13);
		
		// If they are seeking out to edit a specific problem
		if (urlRequest.startsWith("/edit/")){
			
			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			
			if (uuid != null && uuid.length() == 36){
				
				// Verifies that the Viewer is the Author
				String authorUserId = ContentAPI.getContentAuthorId(uuid);
				boolean correctAuthor = authorUserId != null && authorUserId.equals(user.getUserId());
				boolean userIsAdmin = UserServiceFactory.getUserService().isUserAdmin();
				if (correctAuthor || userIsAdmin){
				
					TextContent tc;
					try {
						tc = new TextContent(uuid);
					} catch (EntityNotFoundException e) {
						// If it doesn't yet exist, send a 404.
						resp.setContentType("text/html");
						RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
						jsp.forward(req, resp);
						return;
					}
					
					//If the TextContent is already submitted, redirect the user to the live page with it.
					if (tc.getSubmitted() && !userIsAdmin){	
						resp.sendRedirect("/practice-problem/"+uuid);
					} else {
						// Adds the current TextContent to the context, and prepares it for editing.
						req.setAttribute("textContent", tc);
						resp.setContentType("text/html");
						UserContextAPI.addUserContextToRequest(req, "/contribute/text-content/edit/" + uuid);
						RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/text-content.jsp");
						jsp.forward(req, resp);
					}
				} else {
					resp.setContentType("text/html");
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
					jsp.forward(req, resp);
					return;
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
		
		User submitter = UserServiceFactory.getUserService().getCurrentUser();
		String uuid = req.getParameter("uuid");
		
		if (submitter == null){
			System.err.println("A user not logged in attempted to post text content.");
			return;
		}
		if (uuid != null && !uuid.equals("")){
			String authorUserId = ContentAPI.getContentAuthorId(uuid);
			boolean userIsAdmin = UserServiceFactory.getUserService().isUserAdmin();
			if (!submitter.getUserId().equals(authorUserId) && !userIsAdmin){
				System.err.println("A user ["+submitter.getUserId()+"], not the author ["+authorUserId+"] attempted to modify problem ["+uuid+"].");
				return;
			}
		}
		// If we get here, we have the permissions to proceed.
		
		
		// Saves the new/updated TextContent, and now we have to decide what to next show the user.
		uuid = ContentAPI.createOrUpdateContentFromRequest(req, "textContent");
		
		String saveButtonInstruction = req.getParameter("saveButton");
		
		if (saveButtonInstruction.equals("Save + Preview")){
			// Redirects the user to see a non-live version of their content
			resp.sendRedirect("/contribute/preview/" + uuid);
		} else if (saveButtonInstruction.equals("Save Changes")){
			// Redirects the user to see their past created content
			resp.sendRedirect("/contribute/dashboard");
		} else {
			// Otherwise, we have submitted, so give the user their instant burst of karma
			KarmaAPI.incrementContentKarma(uuid, 5);
			
			// Increment their stats for Achievments
			AchievementsAPI.incrementUserAchievementStatsFromContentSubmission(submitter.getUserId(), req.getParameter("body"), "TextContent");
			
			// and direct them to a thank-you page
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			req.setAttribute("readableContentType", "some interesting content");
			jsp.forward(req, resp);
		}	
	}
}
