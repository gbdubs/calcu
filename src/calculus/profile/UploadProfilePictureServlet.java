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
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

@SuppressWarnings("serial")
public class UploadProfilePictureServlet extends HttpServlet {

	private ImagesService imageService = ImagesServiceFactory.getImagesService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobFields = bs.getUploads(req);
		List<BlobKey> blobKeys = blobFields.get("profilePictureUpload");
		String userId = req.getParameter("userId");
		
		BlobKey newKey = null;
		for (BlobKey blobKey : blobKeys){
			newKey = blobKey;
		}
		if (newKey == null) return;
	
		// Deletes the Blob Key that was previously there to prevent storing files which we no long need.
		BlobKey oldKey = UserPublicInfoAPI.getProfilePictureBlobKey();
		if (oldKey != null) bs.delete(oldKey);
		UserPublicInfoAPI.setProfilePictureBlobKey(newKey);
		
		String servingUrl = imageService.getServingUrl(
				ServingUrlOptions.Builder.withBlobKey(newKey).crop(true).imageSize(300)
		);
       
		UserPublicInfoAPI.setUserProfilePictureServingUrl(servingUrl);
		
		resp.sendRedirect("/user/" + userId);
		
	}
}