package calculus.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.NotificationsAPI;
import calculus.models.Notification;
import calculus.recommendation.PublicRecommendationAPI;
import calculus.utilities.Settings;

@SuppressWarnings("serial")
public class CalculateRecommendationsServlet extends HttpServlet {

	public void doPost(HttpServletRequest req,  HttpServletResponse resp) throws ServletException, java.io.IOException {
		long start = System.currentTimeMillis();
		String userId = (String) req.getParameter("userId");
		if (userId != null){
			PublicRecommendationAPI.refreshRecommendations(userId);
		}
		Notification n = new Notification()
			.withRecipientId(userId)
			.withColor("danger")
			.withTimeNow()
			.withImageUrl("/_static/img/new-notifications.png")
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withUrl("/recommendations")
			.withTitle("New Recommendations Calculated")
			.withBody("Click here to check out your newly calculated recommendations...");
		NotificationsAPI.sendNotification(n);
		
		System.out.println("Recalculated Recommendations For " + userId + " in " + (System.currentTimeMillis() - start) + " milliseconds.");
	}
}
