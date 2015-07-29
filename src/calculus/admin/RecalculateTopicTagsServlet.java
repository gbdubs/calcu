package calculus.admin;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.TopicAPI;

import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RecalculateTopicTagsServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		if (UserServiceFactory.getUserService().isUserAdmin()){
			TopicAPI.recalculateAllTopicTags();
		}
		resp.sendRedirect("/home");
	}
}
