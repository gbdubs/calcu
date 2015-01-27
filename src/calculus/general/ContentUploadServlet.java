package calculus.general;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PracticeProblemAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class ContentUploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest req,  HttpServletResponse resp) throws ServletException, java.io.IOException {
		
		String jsonData = req.getParameter("jsonDataUpload");
		
		JsonParser parser = new JsonParser();
		
		JsonElement result = parser.parse(jsonData);
		boolean convertable = result.isJsonArray();
		
		JsonArray array = null;
		if (convertable){
			array = result.getAsJsonArray();
		}
		
		int i = 0;
		for(i = 0; i < array.size(); i++){
			System.out.println(i);
			JsonObject content = array.get(i).getAsJsonObject();
			String body = content.get("problem").getAsString();
			String title = content.get("title").getAsString();
			String solution = "This problem does not have a solution.";
			if (!content.get("solution").isJsonNull())
				solution = content.get("solution").getAsString();
			String solutionLink = content.get("solutionLink").getAsString();
			String site = content.get("site").getAsString();
			
			String uuid = PracticeProblemAPI.createNewPracticeProblemFromUpload(title, body, solution, "auto-uploaded", solutionLink, site);
			System.out.println("New UUID: " + uuid);
		}
		
		resp.getWriter().print("Successfully added <b>" + i + "</b> pieces of content.");
	}
}