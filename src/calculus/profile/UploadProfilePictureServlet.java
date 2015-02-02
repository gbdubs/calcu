package calculus.profile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPublicInfoAPI;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class UploadProfilePictureServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobFields = bs.getUploads(req);
		List<BlobKey> blobKeys = blobFields.get("profilePictureUpload");
		String userId = req.getParameter("userId");
		
		for (BlobKey blobKey : blobKeys){
			UserPublicInfoAPI.setUserProfilePictureBlobKey(blobKey);
			resp.sendRedirect("/user/" + userId);
			return;
		}
	}	
}
