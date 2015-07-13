package calculus.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class UploadWorker extends HttpServlet {

	public static BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		Gson gson = new Gson();
		
		BlobKey blobKey = new BlobKey(req.getParameter("achievementsBlobKey"));
		System.out.println("Worker being run: " + blobKey);
		
		BlobstoreInputStream bis = new BlobstoreInputStream(blobKey);
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));

		DataUploadPackage dataPackage = gson.fromJson(br, DataUploadPackage.class);
		
		dataPackage.asyncSave();
	}
	
}
