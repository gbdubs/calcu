package calculus.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
					userPrivateInfo.setProperty(property, preferences.get(property));
					changed = true;
				}
			} else {
				System.out.println("USER DATASTORE API ERROR: Unknown Property");
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
			List<String> bookmarks = new ArrayList<String>();
			userPrivateInfo.setProperty("emailKarma", "none");
			userPrivateInfo.setProperty("emailRecommend", "weekly");
			userPrivateInfo.setProperty("emailReply", "none");
			userPrivateInfo.setProperty("bookmarks", bookmarks);
			UserPrivateInfoAPI.datastore.put(userPrivateInfo);
			return userPrivateInfo;
		}
	}

	public static void addUserSkippedContent(String userId, String uuid){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		List<String> skipped = (List<String>) userPrivateInfo.getProperty("answerModeSkipped");
		if (skipped == null) skipped = new ArrayList<String>();
		skipped.add(uuid);
		System.out.println("Got here");
		userPrivateInfo.setProperty("answerModeSkipped", skipped);
		datastore.put(userPrivateInfo);
	}
	
	public static void addUserAnsweredContent(String userId, String uuid){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		List<String> skipped = (List<String>) userPrivateInfo.getProperty("answerModeAnswered");
		if (skipped == null) skipped = new ArrayList<String>();
		skipped.add(uuid);
		userPrivateInfo.setProperty("answerModeAnswered", skipped);
		datastore.put(userPrivateInfo);
	}
	
	public static List<String> getUserAnsweredContentUuids(String userId){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		List<String> result = (List<String>) userPrivateInfo.getProperty("answerModeAnswered");
		if (result != null) return result;
		return new ArrayList<String>();
	}
	
	public static List<String> getUserSkippedContentUuids(String userId){
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(userId);
		List<String> result = (List<String>) userPrivateInfo.getProperty("answerModeSkipped");
		if (result != null) return result;
		return new ArrayList<String>();
	}
}
