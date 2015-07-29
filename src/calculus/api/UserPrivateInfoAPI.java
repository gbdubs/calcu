package calculus.api;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

public class UserPrivateInfoAPI {

	private static Set<String> PRIVATE_FIELDS;
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	static{
		UserPrivateInfoAPI.PRIVATE_FIELDS = new HashSet<String>();
		UserPrivateInfoAPI.PRIVATE_FIELDS.add("emailKarma");
		UserPrivateInfoAPI.PRIVATE_FIELDS.add("emailRecommend");
		UserPrivateInfoAPI.PRIVATE_FIELDS.add("emailReply");
		UserPrivateInfoAPI.PRIVATE_FIELDS.add("bookmarks");
	}
	
	public static void setUserEmailPreferences(User user, Map<String, String> preferences) {
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(user);
		boolean changed = false;
		for (String property : preferences.keySet()){
			if (PRIVATE_FIELDS.contains(property)){
				if (userPrivateInfo.getProperty(property) != preferences.get(property)){
					userPrivateInfo.setUnindexedProperty(property, preferences.get(property));
					changed = true;
				}
			} else {
				System.err.println("USER DATASTORE API ERROR: Unknown Property");
			}
		}
		if (changed){
			datastore.put(userPrivateInfo);
		}
	}

	public static Entity getOrCreateUserPrivateInfo(User user){
		if (user == null) return null;
		return getOrCreateUserPrivateInfo(user.getUserId());
	}

	public static Entity getOrCreateUserPrivateInfo(String userId) {
		Key privateInfoKey = KeyFactory.createKey("UserPrivateInfo", userId);
		
		Entity userPrivateInfo = new Entity(privateInfoKey);
		
		try{
			userPrivateInfo = UserPrivateInfoAPI.datastore.get(privateInfoKey);
			return userPrivateInfo;
		} catch (EntityNotFoundException e){
			userPrivateInfo.setUnindexedProperty("emailKarma", "none");
			userPrivateInfo.setUnindexedProperty("emailRecommend", "weekly");
			userPrivateInfo.setUnindexedProperty("emailReply", "none");
			UserPrivateInfoAPI.datastore.put(userPrivateInfo);
			return userPrivateInfo;
		}
	}

	public static void addUserSkippedContent(String userId, String uuid){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		List<String> skipped = SafeList.string(userPrivateInfo, "answerModeSkipped");
		skipped.add(uuid);
		userPrivateInfo.setUnindexedProperty("answerModeSkipped", skipped);
		datastore.put(userPrivateInfo);
	}
	
	public static void addUserAnsweredContent(String userId, String uuid){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		List<String> skipped = SafeList.string(userPrivateInfo, "answerModeAnswered");
		skipped.add(uuid);
		userPrivateInfo.setUnindexedProperty("answerModeAnswered", skipped);
		datastore.put(userPrivateInfo);
	}
	
	public static List<String> getUserAnsweredContentUuids(String userId){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		return SafeList.string(userPrivateInfo, "answerModeAnswered");
	}
	
	public static List<String> getUserSkippedContentUuids(String userId){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		return SafeList.string(userPrivateInfo, "answerModeSkipped");
	}

	public static boolean userExists(String userId) {
		Key privateInfoKey = KeyFactory.createKey("UserPrivateInfo", userId);
		try {
			datastore.get(privateInfoKey);
			return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

}
