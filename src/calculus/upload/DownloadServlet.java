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
import com.google.appengine.api.utils.SystemProperty;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			UserContextAPI.addUserContextToRequest(req, "/admin/download");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/admin-download.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.sendRedirect("/page-not-found");
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production){
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/admin/download/worker"));
				resp.getWriter().println("Writing Process Initiated.");
			} else {
				DownloadWorker.printAll(resp.getWriter());
			}
		} else {
			resp.sendRedirect("/page-not-found");
		}
	}
}
