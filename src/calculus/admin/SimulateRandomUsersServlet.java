package calculus.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.usermodeling.RandomRatingsUser;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

@SuppressWarnings("serial")
public class SimulateRandomUsersServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String numString = req.getParameter("numberToSimulate");
		int num = Integer.parseInt(numString);
		
		Queue queue = QueueFactory.getQueue("simulateUserQueue");
		for(int i = 0; i < num; i++){
			queue.addAsync(RandomRatingsUser.createNewRandomUser());
		}
		
		resp.sendRedirect("/admin");
	}
}

