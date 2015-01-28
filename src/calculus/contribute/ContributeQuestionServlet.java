package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.QuestionAPI;
import calculus.api.UserContextAPI;
import calculus.api.UserVerificationAPI;
import calculus.models.Question;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class ContributeQuestionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		// Verifies that there is a user for this
		if (!UserVerificationAPI.verifyUserLoggedIn("/contribute/question/new", "Contribute Question Page", req, resp));

		// Finds the request that directs us to the user's aims.
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/question") +  9);
		
		// If they are seeking out to edit a specific problem
		if (urlRequest.startsWith("/edit/")){
			
			String uuid = UuidTools.getUuidFromUrl(urlRequest);
			
			if (uuid != null && uuid.length() == 36){
				
				// TODO: Verify that the User is the Author
				
				Question q = new Question(uuid);
				
				//If the question is already submitted, redirect the user to the live page with it.
				if (q.getSubmitted()){	
					resp.sendRedirect("/practice-problem/"+uuid);
				} else {
					// Adds the current question to the context, and prepares it for editing.
					QuestionAPI.addQuestionContext(req, q);
					resp.setContentType("text/html");
					UserContextAPI.addUserContextToRequest(req, "/contribute/question/edit/" + uuid);
					RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/question.jsp");
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
		
		// TODO: Verify that the User is the Author
		
		// Saves the new/updated question, and now we have to decide what to next show the user.
		String uuid = QuestionAPI.createOrUpdateQuestionFromRequest(req);
		
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
			req.setAttribute("readableContentType", "an interesting question");
			jsp.forward(req, resp);
		}	
	}
}
