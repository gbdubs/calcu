package calculus.general;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.AchievementsAPI;
import calculus.api.UserContextAPI;
import calculus.models.Achievement;

@SuppressWarnings("serial")
public class AdminConsoleServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		// SWAP: if (UserServiceFactory.getUserService().isUserAdmin()){
		if (UserServiceFactory.getUserService().getCurrentUser() != null){
			List<Achievement> achievements = AchievementsAPI.getAllAchievements();
			req.setAttribute("allAchievements", achievements);
			UserContextAPI.addUserContextToRequest(req, "/admin");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/admin-console.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.sendRedirect("/page-not-found");
		}		
	}
}
