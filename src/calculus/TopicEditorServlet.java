package calculus;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.LandingFactsAPI;
import calculus.api.UserContextAPI;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class TopicEditorServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/home");
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/topic-editor.jsp");
		jsp.forward(req, resp);
	}
	
}
