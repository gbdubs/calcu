package calculus.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class ContentUploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest req,  HttpServletResponse resp) throws ServletException, java.io.IOException {
		
		String jsonData = req.getParameter("jsonDataUpload");
		String contentType = req.getParameter("contentType");
		
		JsonParser parser = new JsonParser();
		
		JsonElement result = parser.parse(jsonData);
		boolean convertable = result.isJsonArray();
		
		JsonArray array = null;
		if (convertable){
			array = result.getAsJsonArray();
		} else {
			resp.getWriter().print("Failed to Add Content. Checkpoint 1.");
		}
		
		if (contentType.equals("practiceProblem")){
			int i = 0;
			int questions = 0;
			List<String> uuids = new ArrayList<String>();
			for(i = 0; i < array.size(); i++){
				JsonObject content = array.get(i).getAsJsonObject();
				String body = content.get("problem").getAsString();
				String title = content.get("title").getAsString();
				String tags = content.get("tags").getAsString() + ",auto-uploaded";
				String solutionLink = content.get("solutionLink").getAsString();
				String site = content.get("site").getAsString();	

				if (!content.get("solution").isJsonNull()){
					String solution = content.get("solution").getAsString();
					String uuid = PracticeProblemAPI.createNewPracticeProblemFromUpload(title, body, solution, tags, solutionLink, site);
					uuids.add(uuid);
				} else {
					questions++;
					String uuid = QuestionAPI.createNewQuestionFromUpload(title, body, tags, site);
					uuids.add(uuid);
				}	
			}
			
			resp.getWriter().print("Successfully added <b>" + i + "</b> practice problems. ("+ questions +" of which were treated as questions)\n " + uuids.toString());
		} else if (contentType.equals("question")){
			int i = 0;
			List<String> uuids = new ArrayList<String>();
			for(i = 0; i < array.size(); i++){
				JsonObject content = array.get(i).getAsJsonObject();
				String body = content.get("problem").getAsString();
				String title = content.get("title").getAsString();
				String tags = content.get("tags").getAsString() + ",auto-uploaded";
				String site = content.get("site").getAsString();	
				String uuid = QuestionAPI.createNewQuestionFromUpload(title, body, tags, site);
				uuids.add(uuid);	
			}
			
			resp.getWriter().print("Successfully added <b>" + i + "</b> questions: \n " + uuids.toString());
		} else {
			resp.getWriter().print("Content Type Not Recognized.");
		}
	}
}