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

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/search");
		
		req.setAttribute("practiceProblemsNotFound", false);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
		jsp.forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String tagString = req.getParameter("tagsInput");
		String[] tags = tagString.split(",");
		List<String> uuids = TagAPI.getUuidsResultsOfMultipleTags(tags);
		List<Content> practiceProblems = new ArrayList<Content>();
		
		for (String uuid : uuids){
			try {
				Content c = new Content(uuid);
				String contentType = c.getContentType();
				if (contentType.equals("practiceProblem")){
					practiceProblems.add(c);
				}
			} catch (EntityNotFoundException e) {
				// Don't add to the list if it doesn't exist. Basic stuff, guys.
			}
		}
		req.setAttribute("tags", tagString);
		req.setAttribute("resultPracticeProblems", practiceProblems);
		
		if (practiceProblems.size() == 0){
			req.setAttribute("practiceProblemsNotFound", true);
		} else {
			req.setAttribute("practiceProblemsNotFound", false);
		}
		
		UserContextAPI.addUserContextToRequest(req, "/search");
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
		
		jsp.forward(req, resp);
	}
	
}
