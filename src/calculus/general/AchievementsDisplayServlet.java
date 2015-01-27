package calculus.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievementsAPI;
import calculus.api.UserContextAPI;
import calculus.models.Achievement;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AchievementsDisplayServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/achievements");
		
		List<Achievement> finishedAchievements= new ArrayList<Achievement>();
		List<Achievement> unfinishedAchievements= new ArrayList<Achievement>();
		
		Achievement navigatorAchievement = new Achievement("39f0a4b9-e094-470e-affb-73f8b70b455e");
		finishedAchievements.add(navigatorAchievement);
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user != null){
			unfinishedAchievements.addAll(AchievementsAPI.getUserUnfinishedAchievements(user.getUserId()));
			unfinishedAchievements.remove(navigatorAchievement);
			finishedAchievements.addAll(AchievementsAPI.getUserAchievements(user.getUserId()));
			
		} else {
			unfinishedAchievements.addAll(AchievementsAPI.getAllAchievements());
		}

		req.setAttribute("unfinishedAchievements", unfinishedAchievements);
		req.setAttribute("finishedAchievements", finishedAchievements);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/achievements.jsp");	
		jsp.forward(req, resp);
	}
	
}
