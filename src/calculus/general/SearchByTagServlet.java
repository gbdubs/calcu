package calculus.general;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;

import calculus.api.TagAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;

@SuppressWarnings("serial")
public class SearchByTagServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/about");
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
		jsp.forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String[] tags = req.getParameter("tags").split(",");
		Set<Content> practiceProblems = new HashSet<Content>();
		for(String t : tags){
			List<String> uuids = TagAPI.getUuidsOfTag(t);
			for (String uuid : uuids){
				Content c;
				try {
					c = new Content(uuid);
					String contentType = c.getContentType();
					if (contentType == "practiceProblem"){
						practiceProblems.add(c);
					}
				} catch (EntityNotFoundException e) {
					// Don't add to the list if it doesn't exist. Basic stuff, guys.
				}
			}
		}
		
		req.setAttribute("resultPracticeProblems", practiceProblems);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
		
		jsp.forward(req, resp);
	}
	
}
