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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class UploadWorker extends HttpServlet {

	public static BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String filePath = req.getParameter("fileUrl");	
		
		InputStream is = getServletContext().getResourceAsStream(filePath);

		if (is != null){
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			DataUploadPackage dataPackage = gson.fromJson(br, DataUploadPackage.class);
			
			dataPackage.patchLatex();
			
			dataPackage.cleanForHtml();
			
			dataPackage.asyncSave();
	
		}
	}
}
