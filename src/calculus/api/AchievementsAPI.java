package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import calculus.models.Achievement;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class AchievementsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static String createNewAchievement(String name, String icon, String color, String secondaryColor, String description, String qualification){
		String AchievementId = UUID.randomUUID().toString();
		
		Key key = KeyFactory.createKey("Achievement", AchievementId);
		
		Entity entity = new Entity(key);
		entity.setProperty("name", name);
		entity.setProperty("icon", icon);
		entity.setProperty("color", color);
		entity.setProperty("secondaryColor", secondaryColor);
		entity.setProperty("qualification", qualification);
		entity.setProperty("description", description);
		entity.setProperty("uuid", AchievementId);
		
		datastore.put(entity);
		
		return AchievementId;
	}
	
	public static void giveUserAchievement(String userId, String achievementUuid){
		List<String> achievements = UserPrivateInfoAPI.getUserAchievementUuids(userId);
		if (!achievements.contains(achievementUuid)){
			achievements.add(achievementUuid);
			UserPrivateInfoAPI.setUserAchievementUuids(userId, achievements);
		}

	}
	
	public static List<Achievement> getUserAchievements(String userId){
		return getAchievements(getUserAchievementUuids(userId));
	}
	
	public static List<Achievement> getUserUnfinishedAchievements(String userId){
		return getAchievements(getUnfinishedAchievementUuids(userId));
	}
	
	public static List<Achievement> getAchievements(List<String> uuids){
		List<Achievement> Achievements = new ArrayList<Achievement>();
		for (String uuid : uuids){
			Achievements.add(new Achievement(uuid));
		}
		return Achievements;
	}
	
	private static List<String> getUserAchievementUuids(String userId){
		return UserPrivateInfoAPI.getUserAchievementUuids(userId);
	}
	
	private static List<String> getUnfinishedAchievementUuids(String userId){
		List<String> allAchievements = getAllAchievementUuids();
		List<String> userAchievements = getUserAchievementUuids(userId);
		allAchievements.removeAll(userAchievements);
		return allAchievements;
	}

	private static List<String> getAllAchievementUuids() {
		List<String> result = new ArrayList<String>();
		Query query = new Query("Achievement");
		PreparedQuery pq = datastore.prepare(query);
		for(Entity entity : pq.asIterable()){
			result.add((String) entity.getProperty("uuid"));
		}
		return result;
	}
	
	public static List<Achievement> getAllAchievements(){
		return getAchievements(getAllAchievementUuids());
	}

	public static void deleteAchievement(String uuid) {
		if (uuid != null)
			datastore.delete(KeyFactory.createKey("Achievement", uuid));
	}
}
