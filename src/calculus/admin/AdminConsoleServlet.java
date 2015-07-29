package calculus.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievementsAPI;
import calculus.api.ReportAPI;
import calculus.api.UserContextAPI;
import calculus.models.Achievement;
import calculus.models.Report;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AdminConsoleServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	
		UserService us = UserServiceFactory.getUserService();
		
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			List<Achievement> achievements = AchievementsAPI.getAllAchievements();
			req.setAttribute("allAchievements", achievements);
			List<Report> reportedContent = ReportAPI.getAllOpenReports();
			req.setAttribute("reportedContent", reportedContent);
			
			if (!req.getRequestURI().equals("/admin-safe")){
				UserContextAPI.addUserContextToRequest(req, "/admin");
			}
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/admin-console.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
		}		
	}
}
