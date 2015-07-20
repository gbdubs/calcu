package calculus.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class UploadWorker extends HttpServlet {

	public static BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		Gson gson = new Gson();
		
		String filePath = req.getParameter("fileUrl");	
		
		InputStream is = getServletContext().getResourceAsStream(filePath);

		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		DataUploadPackage dataPackage = gson.fromJson(br, DataUploadPackage.class);
		
		dataPackage.patchLatex();
		
		dataPackage.asyncSave();
	}

	public static void uploadAchievements() {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/admin/upload/worker").param("fileUrl", "/WEB-INF/data/achievements.txt"));
	}
	
	public static void uploadState(){
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/admin/upload/worker").param("fileUrl", "/WEB-INF/data/content/first-round.txt"));
		queue.add(TaskOptions.Builder.withUrl("/admin/upload/worker").param("fileUrl", "/WEB-INF/data/content/second-round.txt"));
	}
	
}
