package calculus.usermodeling;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SimulateUserQueueServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		String interests = req.getParameter("interests");
		System.out.println("THIS TASK WAS SUCCESSFULLY EXECUTED: " + interests);
	}
}
