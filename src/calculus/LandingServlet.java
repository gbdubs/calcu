package calculus;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

import calculus.api.LandingFactsAPI;
import calculus.api.UserContextAPI;

@SuppressWarnings("serial")
public class LandingServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/home");
		
		req.setAttribute("ad", false);
		Entity entity = LandingFactsAPI.getOrRefreshLandingEntity();
		int numUsers = LandingFactsAPI.getNumUsers(entity);
		int recentPosts = LandingFactsAPI.getNumRecentPosts(entity);
		int piecesOfContent = LandingFactsAPI.getNumPiecesOfContent(entity);
		
		req.setAttribute("numUsers", numUsers);
		req.setAttribute("recentPosts", recentPosts);
		req.setAttribute("piecesOfContent", piecesOfContent);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/landing.jsp");
		jsp.forward(req, resp);
	}
	
}
