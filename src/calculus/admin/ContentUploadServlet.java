package calculus.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class ContentUploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest req,  HttpServletResponse resp) throws ServletException, java.io.IOException {
		
		if (!UserServiceFactory.getUserService().isUserAdmin()){
			return;
		}
		String jsonData = req.getParameter("jsonDataUpload");
		
	}
	
	public static boolean uploadContentInJsonForm(String jsonData){
		JsonParser parser = new JsonParser();
		
		JsonElement result = parser.parse(jsonData);
		boolean convertable = result.isJsonArray();
		
		JsonArray array = null;
		if (convertable){
			array = result.getAsJsonArray();
		} else {
			return false;
		}
		
		return false;
	}
	
	
		
}