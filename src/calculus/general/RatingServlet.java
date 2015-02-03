package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.KarmaAPI;
import calculus.api.RatingsAPI;

@SuppressWarnings("serial")
public class RatingServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String raterId = req.getParameter("userId");
		String contentUuid = req.getParameter("contentUuid");
		String helpfulness = req.getParameter("helpfulness");
		String difficulty = req.getParameter("difficulty");
		String quality = req.getParameter("quality");
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null || raterId == null || !user.getUserId().equals(raterId)) return;
		
		try {
			int h = Integer.parseInt(helpfulness);
			int d = Integer.parseInt(difficulty);
			int q = Integer.parseInt(quality);
			RatingsAPI.submitRating(contentUuid, raterId, h,d,q);
			// Give the Rater instant karma for submitting the rating
			KarmaAPI.incrementUserKarmaForRatingOthers(raterId, 1);
		} catch (NumberFormatException nfe){
			return;
		}
	}
}
