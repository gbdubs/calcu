package calculus.topic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.UserContextAPI;
import calculus.models.Topic;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class TopicEditorServlet extends HttpServlet {
	
	private static UserService userService = UserServiceFactory.getUserService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		boolean isAdmin = userService.isUserLoggedIn() && userService.isUserAdmin();
		
		if (isAdmin){
			String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
			try {
				Topic t = new Topic(uuid);
				req.setAttribute("topic", t);
				UserContextAPI.addUserContextToRequest(req, "/topic-editor");
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/topic-editor.jsp");
				jsp.forward(req, resp);
				return;
			} catch (EntityNotFoundException e) {
				// Follow through to the not found page
			}
		}
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		jsp.forward(req, resp);
		return;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		boolean isAdmin = userService.isUserLoggedIn() && userService.isUserAdmin();
		
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		if (uuid != null && isAdmin){
			try {
				Topic t = new Topic(uuid);
				List<String> subTopics = Arrays.asList(req.getParameter("subTopics").split(","));
				List<String> parentTopics = Arrays.asList(req.getParameter("subTopics").split(","));
				String title = req.getParameter("title");
				String shortDescription = req.getParameter("shortDescription");
				String longDescription = req.getParameter("longDescription");
				String tags = req.getParameter("tags");
				int difficulty = Integer.parseInt(req.getParameter("difficulty"));
				for (String subTopic : subTopics){
					t.addSubTopic(subTopic);
				}
				for (String parentTopic : parentTopics){
					t.addParentTopic(parentTopic);
				}
				t.setTitle(title);
				t.setShortDescription(shortDescription);
				t.setLongDescription(longDescription);
				t.setTags(tags);
				t.setDifficulty(difficulty);
				t.save();
				resp.sendRedirect("/topic/" + uuid);
				return;
			} catch (EntityNotFoundException e) {
				// Throw the not found page.
			}
		}
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		jsp.forward(req, resp);
		return;
	}
}