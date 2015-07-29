package calculus.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
		
		UserContextAPI.addUserContextToRequest(req, "/explore");
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String userId = "";
		if (user != null) userId = user.getUserId();
		
		Collection<Content> sampleContent = ContentAPI.getContentSampling(24, 0, userId);
		ArrayList<Content> randomContent = new ArrayList<>();
		randomContent.addAll(sampleContent);
		
		ArrayList<Content> exploratoryContent = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			exploratoryContent.add(randomContent.get(i));
		}
		
		ArrayList<Content> exploratoryContent1 = new ArrayList<>();
		for (int i = 8; i < 16; i++) {
			exploratoryContent1.add(randomContent.get(i));
		}
		
		ArrayList<Content> exploratoryContent2 = new ArrayList<>();
		for (int i = 16; i < 24; i++) {
			exploratoryContent2.add(randomContent.get(i));
		}
		
		req.setAttribute("exploreContent", exploratoryContent);
		req.setAttribute("exploreContent1", exploratoryContent1);
		req.setAttribute("exploreContent2", exploratoryContent2);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/explore.jsp");	
		jsp.forward(req, resp);
	}
	
}
