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
		
		finishedAchievements.add(new Achievement("4ac476d9-31eb-4116-8f10-dbacd28781bf"));
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user != null){
			unfinishedAchievements.addAll(AchievementsAPI.getUserUnfinishedAchievements(user.getUserId()));
			finishedAchievements.addAll(AchievementsAPI.getUserAchievements(user.getUserId()));
		} else {
			unfinishedAchievements.addAll(AchievementsAPI.getAllAchievements());
		}
		System.out.println("unfinished: " + unfinishedAchievements);
		System.out.println("finished: " + finishedAchievements);
		req.setAttribute("unfinishedAchievements", unfinishedAchievements);
		req.setAttribute("finishedAchievements", finishedAchievements);
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/achievements.jsp");	
		jsp.forward(req, resp);
	}
	
}
