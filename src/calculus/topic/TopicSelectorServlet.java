package calculus.topic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

@SuppressWarnings("serial")
public class TopicSelectorServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/topic-selector");
		
		req.setAttribute("allTopics", TopicAPI.getAllTopics());
		req.setAttribute("rootTopics", TopicAPI.getAllRootTopics());
		
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/topic-selector.jsp");
		jsp.forward(req, resp);
	}
}
