package calculus.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPublicInfoAPI;
import calculus.recommendation.MasterRecommendationsAPI;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public class RecalculateRecommendationsServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String userString = req.getParameter("user");
		if (userString.equals("") || userString == null){
			resp.sendRedirect("/admin");
		}
		Queue queue = QueueFactory.getQueue("calculateRecommendationsQueue");
		if (userString.startsWith("FIRST ")) {
			userString = userString.substring(6);
			int numToCalc = Integer.parseInt(userString);
			List<String> toCalc = MasterRecommendationsAPI.getUsersInNeedOfRecalculation(numToCalc);
			for(String userId : toCalc){
				queue.addAsync(updateRecommendationsForUser(userId));
			}
	    } else if (userString.equals("ALL USERS")) {
			List<String> allUserIds = UserPublicInfoAPI.getAllUserIds();
			System.out.println("Adding " + allUserIds.size() + " users to the calculation queue.");
			for(String userId : allUserIds){
				queue.addAsync(updateRecommendationsForUser(userId));
			}
		} else {
			queue.addAsync(updateRecommendationsForUser(userString));
		}
		
		resp.sendRedirect("/admin");
	}
	
	public static TaskOptions updateRecommendationsForUser(String userId){
		return TaskOptions.Builder.withUrl("/recommendations/calculate").param("userId", userId);
	}
}

