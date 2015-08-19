package calculus.upload;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	
	private static final boolean TEST_MODE = false;
	private static final int TEST_UPLOADS = 1;
	private static final int DIGESTABLE_UPLOADS = 256;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			UserContextAPI.addUserContextToRequest(req, "/admin/upload");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/admin-upload.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			UploadServlet.uploadState();
			resp.sendRedirect("/home");
		} else {
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		}
	}

	public static void uploadState(){
		Queue queue = QueueFactory.getQueue("uploadQueue");
		int max = DIGESTABLE_UPLOADS;
		String url = "/WEB-INF/data/content/digestable/";
		if (TEST_MODE){
			max = TEST_UPLOADS;
			url = "/WEB-INF/data/content/test/";
		}
		for (int i = 1; i <= max; i++){
			queue.add(TaskOptions.Builder.withUrl("/admin/upload/worker").param("fileUrl", url+i+".txt"));
		}
	}

	public static void uploadAchievements() {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/admin/upload/worker").param("fileUrl", "/WEB-INF/data/achievements.txt"));
	}
}
