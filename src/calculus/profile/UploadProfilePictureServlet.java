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
		
		BlobKey result = null;
		for (BlobKey blobKey : blobKeys){
			result = blobKey;
		}
		if (result == null) return;
	
		String servingUrl = imageService.getServingUrl(
				ServingUrlOptions.Builder.withBlobKey(result).crop(true).imageSize(300)
		);
       
		UserPublicInfoAPI.setUserProfilePictureServingUrl(servingUrl);
		
		resp.sendRedirect("/user/" + userId);
		
	}
}