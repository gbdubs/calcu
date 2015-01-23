package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AchievmentsAPI {

	private static List<String> allAchievments;
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	static {
		String[] achievments = {"Beginner", "Helper", "Expert"};
		for(String a : achievments){
			allAchievments.add(a);
		}
	}
	
	public static String createNewAchievment(String name, String icon, String color, String secondaryColor, String description, String qualification){
		String achievmentId = UUID.randomUUID().toString();
		
		Key key = KeyFactory.createKey("Achievment", achievmentId);
		
		Entity entity = new Entity(key);
		entity.setProperty("name", name);
		entity.setProperty("icon", icon);
		entity.setProperty("color", color);
		entity.setProperty("secondaryColor", secondaryColor);
		entity.setProperty("qualification", qualification);
		entity.setProperty("decription", description);
		entity.setProperty("uuid", achievmentId);
		
		return achievmentId;
	}
	
	public static void giveUserAchievement(String userId, String achievmentUuid){
		Entity userPrivateInfo = UserPrivateInfoAPI.getOrCreateUserPrivateInfo(userId);
		List<String> achievments = (List<String>) userPrivateInfo.getProperty("achievments");
		if (achievments == null) achievments = new ArrayList<String>();
		achievments.add(achievmentUuid);
		userPrivateInfo.setProperty("achievments", achievments);
		datastore.put(userPrivateInfo);
	}
	
}
