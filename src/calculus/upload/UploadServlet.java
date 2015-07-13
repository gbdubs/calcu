package calculus.upload;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			
			BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
			UploadOptions uploadOptions = UploadOptions.Builder.withMaxUploadSizeBytes(20 * 1024L * 1024L);
			String uploadUrl = bs.createUploadUrl("/admin/upload", uploadOptions);
			
			req.setAttribute("blobstoreUploadUrl", uploadUrl);
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
		System.out.println("Handler Invoked.");
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			System.out.println("Handler Initiated");
			BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
			Map<String, List<BlobKey>> blobFields = bs.getUploads(req);
			List<BlobKey> blobKeys = blobFields.get("stateUpload");
			
			BlobKey newKey = null;
			for (BlobKey blobKey : blobKeys){
				newKey = blobKey;
			}
			if (newKey == null){
				resp.getWriter().println("There was no Blob Key Specified.");
				return;
			}
			
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/admin/upload/worker").param("blobKey", newKey.getKeyString()));
			resp.sendRedirect("/home");
			
		} else {
			resp.sendRedirect("/page-not-found");
		}
	}
}
