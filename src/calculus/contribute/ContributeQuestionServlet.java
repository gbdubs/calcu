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
import calculus.models.Question;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContributeQuestionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		// If the user is not logged in, redirect them to a page which describes that the page dictates a login
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/question/new");
			resp.setContentType("text/html");
			req.setAttribute("pageName", "Question Creation");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}

		// Finds the request that directs us to the user's aims.
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/question") +  9);
		
		// If they are seeking out to edit a specific problem
		if (urlRequest.startsWith("/edit/")){
			
			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			
			if (uuid != null && uuid.length() == 36){
				
				// Verifies that the Viewer is the Author
				String authorUserId = ContentAPI.getContentAuthorId(uuid);
				boolean correctAuthor = authorUserId != null;
				if (correctAuthor) {
					correctAuthor = authorUserId.equals(user.getUserId());
				}
				boolean userIsAdmin = UserServiceFactory.getUserService().isUserAdmin();
				if (correctAuthor || userIsAdmin){				
				
					Question q;
					try {
						q = new Question(uuid);
					} catch (EntityNotFoundException e) {
						resp.setContentType("text/html");
						RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
						jsp.forward(req, resp);
						return;
					}
					//If the question is already submitted, redirect the user to the live page with it.
					if (q.getSubmitted() && !userIsAdmin){	
						resp.sendRedirect("/question/"+uuid);
					} else {
						// Adds the current question to the context, and prepares it for editing.
						req.setAttribute("question", q);
						resp.setContentType("text/html");
						UserContextAPI.addUserContextToRequest(req, "/contribute/question/edit/" + uuid);
						RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/question.jsp");
						jsp.forward(req, resp);
					}
					
				} else {
					resp.setContentType("text/html");
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
					jsp.forward(req, resp);
				}
				return;
			}
		}
		
		// If any section of the above fails, we don't have a specific problem to edit, so edit a blank one
		UserContextAPI.addUserContextToRequest(req, "/contribute/question/new");
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/question.jsp");
		jsp.forward(req, resp);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		User submitter = UserServiceFactory.getUserService().getCurrentUser();
		String uuid = req.getParameter("uuid");
		
		if (submitter == null){
			System.err.println("A user not logged in attempted to post a quesiton.");
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
		
		// Saves the new/updated question, and now we have to decide what to next show the user.
		uuid = ContentAPI.createOrUpdateContentFromRequest(req, "question");
		
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
			AchievementsAPI.incrementUserAchievementStatsFromContentSubmission(submitter.getUserId(), req.getParameter("body"), "Questions");
			
			// And thank the user for their contribution
			resp.setContentType("text/html");
			UserContextAPI.addUserContextToRequest(req, "/contribute/dashboard");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");
			req.setAttribute("readableContentType", "an interesting question");
			jsp.forward(req, resp);
		}	
	}
}
