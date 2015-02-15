package calculus.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user != null){
			unfinishedAchievements.addAll(AchievementsAPI.getAllAchievements());
			finishedAchievements.addAll(AchievementsAPI.getUserAchievements(user.getUserId()));
		} else {
			unfinishedAchievements.addAll(AchievementsAPI.getAllAchievements());
		}
		
		finishedAchievements.add(unfinishedAchievements.remove(0));
		
		Map<Achievement, Boolean> allAchievements = new HashMap<Achievement, Boolean>();
		for (Achievement a : finishedAchievements) allAchievements.put(a, true);
		for (Achievement a : unfinishedAchievements) allAchievements.put(a, false);
		req.setAttribute("achievements", allAchievements);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/achievements.jsp");	
		jsp.forward(req, resp);
	}
	
}
