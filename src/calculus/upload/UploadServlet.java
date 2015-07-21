package calculus.upload;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	
	private static final int DIGESTABLE_UPLOADS = 112;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			UserContextAPI.addUserContextToRequest(req, "/admin/upload");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/admin-upload.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.sendRedirect("/page-not-found");
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			UploadWorker.uploadState(DIGESTABLE_UPLOADS);
			resp.sendRedirect("/home");
		} else {
			resp.sendRedirect("/page-not-found");
		}
	}
}
