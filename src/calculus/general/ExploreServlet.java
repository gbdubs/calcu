package calculus.general;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ExploreServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/about");
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String userId = "";
		if (user != null) userId = user.getUserId();
		
		Map<String, List<Content>> exploratoryContent = ContentAPI.getExploratoryContent(24, 0, userId);
		
		List<Content> newContent = exploratoryContent.get("new");
		List<Content> randomContent = exploratoryContent.get("random");
		List<Content> bestContent = exploratoryContent.get("best");
		List<Content> suggestedContent = exploratoryContent.get("suggested");
		
		req.setAttribute("newContent", newContent);
		req.setAttribute("randomContent", randomContent);
		req.setAttribute("bestContent", bestContent);
		req.setAttribute("suggestedContent", suggestedContent);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/explore.jsp");	
		jsp.forward(req, resp);
	}
	
}
