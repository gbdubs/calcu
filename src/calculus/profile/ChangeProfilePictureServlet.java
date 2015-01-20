package calculus.profile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import calculus.utilities.UrlGenerator;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;


public class ChangeProfilePictureServlet extends HttpServlet{

	private ImagesService imageService;
	private static final int THUMBNAILSIZE = 150;
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		imageService = ImagesServiceFactory.getImagesService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
	    InputStream fileContent = getInputStreamFromFileUpload(req);
	    Image originalImage = getImageFromInputStream(fileContent);
	    Image centeredSquaredImage = centerAndSquareImage(originalImage);       
	    Image perfectImage = resizeSquareImage(centeredSquaredImage, THUMBNAILSIZE);         
	
	    
	    
	}                
	               
	private Image resizeSquareImage(Image image, int size) {
		Transform resize = ImagesServiceFactory.makeResize(200, 200, true);
        return imageService.applyTransform(resize, image);
	}

	private Image centerAndSquareImage(Image oldImage){
		int width = oldImage.getWidth();
        int height = oldImage.getHeight();
        
        Transform centeredSquareCrop = null;
        
        if (width < height){
        	double diffProportion = (((double) height / width) - 1) / 2;
        	centeredSquareCrop = ImagesServiceFactory.makeCrop(0, diffProportion, 1, 1 - diffProportion);
        } else if (height < width){
        	double diffProportion = (((double) width / height) - 1) / 2;
        	centeredSquareCrop = ImagesServiceFactory.makeCrop(diffProportion, 0, 1 - diffProportion, 1);
        } else {
        	centeredSquareCrop = ImagesServiceFactory.makeCrop(0, 0, 1, 1);
        }
        
        return imageService.applyTransform(centeredSquareCrop, oldImage);
	}
	
	private Image getImageFromInputStream(InputStream inputStream) throws IOException{
		byte[] fileData = IOUtils.toByteArray(inputStream);      
         
        Image oldImage = ImagesServiceFactory.makeImage(fileData);
		return oldImage;
	}
		
	
	private InputStream getInputStreamFromFileUpload(HttpServletRequest req) throws IOException, ServletException{
		try {
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
	        for (FileItem item : items) {
	            if (!item.isFormField()) {
	            	// Process form file field (input type="file").
	                InputStream fileContent = item.getInputStream();
	                return fileContent;
	            } else {
	            	// Skip: We should only be concerned with the file upload field.
	            }
	        }
	    } catch (FileUploadException e) {
	        throw new ServletException("Cannot parse multipart request.", e);
	    }
		return null;
	}
}
