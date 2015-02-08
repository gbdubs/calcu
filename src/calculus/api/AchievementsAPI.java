package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import calculus.models.Achievement;
import calculus.models.Notification;
import calculus.utilities.Settings;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class AchievementsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static String createNewAchievement(String name, String icon, String color, String secondaryColor, String description, String qualification){
		String AchievementId = UUID.randomUUID().toString();
		
		Key key = KeyFactory.createKey("Achievement", AchievementId);
		
		Entity entity = new Entity(key);
		entity.setUnindexedProperty("name", name);
		entity.setUnindexedProperty("icon", icon);
		entity.setUnindexedProperty("color", color);
		entity.setUnindexedProperty("secondaryColor", secondaryColor);
		entity.setUnindexedProperty("qualification", qualification);
		entity.setUnindexedProperty("description", description);
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
		Notification achievementAlert = createAchievementAlert(userId, achievementUuid);
		NotificationsAPI.sendNotification(achievementAlert);
	}
	
	private static Notification createAchievementAlert(String userId, String achievementUuid) {
		Achievement a = new Achievement(achievementUuid);
		String title = "New Achievement!";
		String body = "You just won the " +a.getName() + " achievement! Check them out here!";
		String url = "/achievements";
		
		
		Notification n = new Notification()
			.withRecipientId(userId)
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withTimeNow()
			.withTitle(title)
			.withBody(body)
			.withUrl(url)
			.withColor("warning");
		
		return n;
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
			try{
				Achievements.add(new Achievement(uuid));
			} catch (RuntimeException e){
				// Ignore this, it is likely a datastore update issue.
			}
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

	public static void deleteAllAchievements() {
		Query query = new Query("Achievement");
		PreparedQuery pq = datastore.prepare(query);
		for(Entity entity : pq.asIterable()){
			datastore.delete(entity.getKey());
		}
	}
}
