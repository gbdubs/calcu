package calculus.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.TagAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;

import com.google.appengine.api.datastore.EntityNotFoundException;

@SuppressWarnings("serial")
public class SearchByTagServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String requestUri = req.getRequestURI();
		UserContextAPI.addUserContextToRequest(req, requestUri);
		
		String tagString = getTagStringFromUrlPattern(requestUri);
		
		if (tagString == null){
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
			jsp.forward(req, resp);
		} else {
			renderSearchResultsForString(tagString, req, resp);
			return;
		}
	}
	
	private String getTagStringFromUrlPattern(String requestUri) {
		int index = requestUri.indexOf("/search/") + 8;
		if (index < 8) return null;
		return requestUri.substring(index).replaceAll("%20", " ");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String tagString = req.getParameter("tagsInput");
		resp.sendRedirect("/search/" + tagString);
	}
	
	private void renderSearchResultsForString(String tagString, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<Content> practiceProblems = new ArrayList<Content>();
		List<Content> questions = new ArrayList<Content>();
		List<Content> textContent = new ArrayList<Content>();
		
		if(!tagString.trim().equals("") && tagString != null){
			String[] tags = tagString.trim().split(",");
			
			List<String> uuids = TagAPI.getUuidsResultsOfMultipleTags(tags);
			
			for (String uuid : uuids){
				try {
					Content c = new Content(uuid);
					String contentType = c.getContentType();
					if (contentType.equals("practiceProblem")){
						practiceProblems.add(c);
					} else if (contentType.equals("question")){
						questions.add(c);
					} else if (contentType.equals("textContent")){
						textContent.add(c);
					}
				} catch (EntityNotFoundException e) {
					// Don't add to the list if it doesn't exist. Basic stuff, guys.
				}
			}
		}
		req.setAttribute("tags", tagString);
		req.setAttribute("resultPracticeProblems", practiceProblems);
		req.setAttribute("resultQuestions", questions);
		req.setAttribute("resultTextContent", textContent);
		
		if (practiceProblems.size() == 0){
			req.setAttribute("practiceProblemsNotFound", true);
		} else {
			req.setAttribute("practiceProblemsNotFound", false);
		}
		
		if (questions.size() == 0){
			req.setAttribute("questionsNotFound", true);
		} else {
			req.setAttribute("questionsNotFound", false);
		}
		
		if (textContent.size() == 0){
			req.setAttribute("textContentNotFound", true);
		} else {
			req.setAttribute("textContentNotFound", false);
		}
		
		UserContextAPI.addUserContextToRequest(req, "/search/" + tagString);
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
		
		jsp.forward(req, resp);
	}
	
}
