package calculus.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.models.PotentialContent;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class UploadPotentialContentServlet extends HttpServlet {

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
			resp.getWriter().print("Failed to Add Potential Content.");
		}
		
		for(int i = 0; i < array.size(); i++){
			JsonObject content = array.get(i).getAsJsonObject();
			String title = content.get("title").getAsString();
			String body = content.get("body").getAsString();
			String solution = content.get("solution").getAsString();
			String tags = content.get("tags").getAsString();
			String source = content.get("source").getAsString();	
			
			PotentialContent pc = new PotentialContent().withTitle(title).withBody(body).withSolution(solution).withTags(tags).withSource(source);
			pc.save();
		}
		
		resp.sendRedirect("/admin");
	}	
}
