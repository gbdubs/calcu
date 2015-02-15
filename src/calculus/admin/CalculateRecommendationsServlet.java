package calculus.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserServiceFactory;

import calculus.recommendation.MasterRecommendationsAPI;

@SuppressWarnings("serial")
public class CalculateRecommendationsServlet extends HttpServlet {

	public void doPost(HttpServletRequest req,  HttpServletResponse resp) throws ServletException, java.io.IOException {
		long start = System.currentTimeMillis();
		String userId = (String) req.getParameter("userId");
		if (userId != null){
			MasterRecommendationsAPI.refreshRecommendations(userId);
		}
		System.out.println("Recalculated Recommendations For " + userId + " in " + (System.currentTimeMillis() - start) + " milliseconds.");
	}
}
