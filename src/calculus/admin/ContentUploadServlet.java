package calculus.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.models.Content;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class ContentUploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest req,  HttpServletResponse resp) throws ServletException, java.io.IOException {
		
		if (!UserServiceFactory.getUserService().isUserAdmin()){
			return;
		}
		
		String jsonData = req.getParameter("jsonDataUpload");
		JsonParser parser = new JsonParser();
		JsonElement result = parser.parse(jsonData);
		boolean convertable = result.isJsonArray();
		JsonArray array = null;
		if (convertable){
			array = result.getAsJsonArray();
		} else {
			resp.getWriter().print("Failed to Add Content. Checkpoint 1.");
		}
		
		int i = 0;
		List<String> uuids = new ArrayList<String>();
		for(i = 0; i < array.size(); i++){
			JsonObject content = array.get(i).getAsJsonObject();
			Content c = ContentAPI.constructContentFromJson(content);
			uuids.add(c.getUuid());
		}
			
		resp.getWriter().print("Successfully added <b>" + i + "</b> pieces of content.");
	}
}