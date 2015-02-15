package calculus.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPublicInfoAPI;

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
		if (userString == "ALL USERS"){
			Queue queue = QueueFactory.getQueue("calculateRecommendationsQueue");
			List<String> allUserIds = UserPublicInfoAPI.getAllUserIds();
			for(String userId : allUserIds){
				queue.addAsync(updateRecommendationsForUser(userId));
			}
		} else {
			Queue queue = QueueFactory.getQueue("calculateRecommendationsQueue");
			queue.addAsync(updateRecommendationsForUser(userString));
		}
		
		resp.sendRedirect("/admin");
	}
	
	public static TaskOptions updateRecommendationsForUser(String userId){
		return TaskOptions.Builder.withUrl("/recommendations/calculate").param("userId", userId);
	}
}

