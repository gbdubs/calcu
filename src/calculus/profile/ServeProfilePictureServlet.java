package calculus.profile;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserPublicInfoAPI;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class ServeProfilePictureServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		
		String uri = req.getRequestURI();
		int index = uri.indexOf("/fetch-profile-picture/");
		String userId = uri.substring(index + 24);
		
		BlobKey bk = UserPublicInfoAPI.getUserProfilePictureBlobKey(userId);
		
		if (bk != null){
			bs.serve(bk, resp);
		} else {
			resp.sendRedirect("/_static/img/default-avatar.png");
		}
	}
}
