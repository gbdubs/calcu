package calculus.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievementsAPI;
import calculus.models.Achievement;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class AchievementModificationServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if (!UserServiceFactory.getUserService().isUserAdmin()){
			return;
		}
		
		String action = req.getParameter("action");
		
		if (action.equals("delete")){
			AchievementsAPI.deleteAchievement(req.getParameter("uuid"));
		} else if (action.equals("create")){
			String method = req.getParameter("uploadMethod");
			if (method.equals("single")){
				String name = req.getParameter("name");
				String description = req.getParameter("description");
				String qualification = req.getParameter("qualification");
				String icon = req.getParameter("icon");
				String color = req.getParameter("color");
				String secondaryColor = req.getParameter("secondaryColor");
				
				if (!name.equals("")){
					AchievementsAPI.createNewAchievement(name, icon, color, secondaryColor, description, qualification);
				}
			} else if (method.equals("bulk")){
				String jsonData = req.getParameter("jsonDataUpload");
				String instructions = req.getParameter("bulkAddInstructions");
				if (instructions.equals("purge")){
					AchievementsAPI.deleteAllAchievements();
				}
				
				JsonParser parser = new JsonParser();
				
				JsonElement result = parser.parse(jsonData);
				boolean convertable = result.isJsonArray();
				
				JsonArray array = null;
				if (convertable){
					array = result.getAsJsonArray();
					List<String> uuids = new ArrayList<String>();
					for(int i = 0; i < array.size(); i++){
						JsonObject achievement = array.get(i).getAsJsonObject();
						String uuid = achievement.get("uuid").getAsString();
						String name = achievement.get("name").getAsString();
						String description = achievement.get("description").getAsString();
						String qualification = achievement.get("qualification").getAsString();
						String icon = achievement.get("icon").getAsString();
						String textColor = achievement.get("textColor").getAsString();
						String backgroundColor = achievement.get("backgroundColor").getAsString();
		
						if (uuid == null){
							uuid = AchievementsAPI.createNewAchievement(name, icon, textColor, backgroundColor, description, qualification);
						} else {
							AchievementsAPI.createNewAchievementWithUuid(uuid, name, icon, textColor, backgroundColor, description, qualification);
						}
						uuids.add(uuid);
					}
				}				
			}
			boolean displayAllAsJson = req.getParameter("displayAllAsJson") != null; 
			if (displayAllAsJson){
				String json = getAllAchievementsAsJson();
				resp.getWriter().println(json);
				return;
			} else {
				resp.sendRedirect("/admin");
			}
		}
	}
	
	public static String getAllAchievementsAsJson() {
		List<Achievement> all = AchievementsAPI.getAllAchievements();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(all);
		return json;
	}
}
