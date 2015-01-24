package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import calculus.models.Achievment;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class AchievmentsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static String createNewAchievment(String name, String icon, String color, String secondaryColor, String description, String qualification){
		String achievmentId = UUID.randomUUID().toString();
		
		Key key = KeyFactory.createKey("Achievment", achievmentId);
		
		Entity entity = new Entity(key);
		entity.setProperty("name", name);
		entity.setProperty("icon", icon);
		entity.setProperty("color", color);
		entity.setProperty("secondaryColor", secondaryColor);
		entity.setProperty("qualification", qualification);
		entity.setProperty("description", description);
		entity.setProperty("uuid", achievmentId);
		
		datastore.put(entity);
		
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
	
	public static List<Achievment> getUserAchievments(String userId){
		return getAchievments(getUserAchievmentUuids(userId));
	}
	
	public static List<Achievment> getUserUnfinishedAchievments(String userId){
		return getAchievments(getUnfinishedAchievementUuids(userId));
	}
	
	public static List<Achievment> getAchievments(List<String> uuids){
		List<Achievment> achievments = new ArrayList<Achievment>();
		for (String uuid : uuids){
			achievments.add(new Achievment(uuid));
		}
		return achievments;
	}
	
	private static List<String> getUserAchievmentUuids(String userId){
		Entity userPrivateInfo = UserPrivateInfoAPI.getOrCreateUserPrivateInfo(userId);
		List<Achievment> achievments = new ArrayList<Achievment>();
		return (List<String>) userPrivateInfo.getProperty("achievments");
	}
	
	private static List<String> getUnfinishedAchievementUuids(String userId){
		List<String> allAchievments = getAllAchievmentUuids();
		allAchievments.removeAll(getUserAchievmentUuids(userId));
		return allAchievments;
	}

	private static List<String> getAllAchievmentUuids() {
		List<String> result = new ArrayList<String>();
		Query query = new Query("Achievment");
		PreparedQuery pq = datastore.prepare(query);
		for(Entity entity : pq.asIterable()){
			result.add((String) entity.getProperty("uuid"));
		}
		return result;
	}
	
	public static List<Achievment> getAllAchievements(){
		return getAchievments(getAllAchievmentUuids());
	}

	public static void deleteAchievement(String uuid) {
		if (uuid != null)
			datastore.delete(KeyFactory.createKey("Achievment", uuid));
	}
}
