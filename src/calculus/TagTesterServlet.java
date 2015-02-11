package calculus;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;
import calculus.usermodeling.RandomUser;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

@SuppressWarnings("serial")
public class TagTesterServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/tag-tester");
		
		resp.setContentType("text/html");
		
		Queue defaultQueue = QueueFactory.getQueue("simulateUserQueue");
		defaultQueue.add(RandomUser.createNewRandomUser());
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/successpage.jsp");
		jsp.forward(req, resp);
	}
}
