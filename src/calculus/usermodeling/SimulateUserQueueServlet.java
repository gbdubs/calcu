package calculus.usermodeling;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SimulateUserQueueServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		String interests = req.getParameter("interests");
		String numActions = req.getParameter("numberOfActions");
		String relativeSkill = req.getParameter("relativeSkill");
		
		System.out.println("THIS TASK WAS SUCCESSFULLY STARTED: " + interests);
		
		RandomRatingsUser.executeRatingsOprations(interests, numActions, relativeSkill);
		
		System.out.println("THIS TASK WAS SUCCESSFULLY EXECUTED: " + interests);
	}
}
