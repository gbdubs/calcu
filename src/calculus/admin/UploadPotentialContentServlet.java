package calculus.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.utilities.Cleaner;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import deprecated.PotentialContent;

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
			
			String title = getStringFromJsonObject(content, "title");
			String body = getStringFromJsonObject(content, "body");
			String solution = getStringFromJsonObject(content, "solution");
			String tags = getStringFromJsonObject(content, "tags");
			String source = getStringFromJsonObject(content, "source");
			
			title = Cleaner.autoclave(title);
			body = Cleaner.cleanHtml(body);
			solution = Cleaner.cleanHtml(solution);
			tags = Cleaner.autoclave(tags);	
			
			PotentialContent pc = new PotentialContent().withTitle(title).withBody(body).withSolution(solution).withTags(tags).withSource(source);
			pc.save();
		}
		
		resp.sendRedirect("/admin");
	}	
	
	private static String getStringFromJsonObject(JsonObject o, String prop){
		JsonElement js = o.get(prop);
		if (js == null || js.isJsonNull()) return "";
		return js.getAsString();
	}
}
