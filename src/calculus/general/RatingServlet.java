package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.RatingsAPI;

@SuppressWarnings("serial")
public class RatingServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String userId = req.getParameter("userId");
		String contentUuid = req.getParameter("contentUuid");
		String helpfulness = req.getParameter("helpfulness");
		String difficulty = req.getParameter("difficulty");
		String quality = req.getParameter("quality");
		
		if (userId != null){
			try {
				int h = Integer.parseInt(helpfulness);
				int d = Integer.parseInt(difficulty);
				int q = Integer.parseInt(quality);
				RatingsAPI.submitRating(contentUuid, userId, h,d,q);
			} catch (NumberFormatException nfe){
				return;
			}
		}
	}
}
