package utilities;

import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

public class UserInitializer {

	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	
	public static Entity getOrCreateUserPublicInfo(User user){
		
		if (user == null) return null;
		
		String userId = user.getUserId();
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", userId);
		
		Entity userPublicInfo = new Entity(publicInfoKey);
		
		try{
			userPublicInfo = datastoreService.get(publicInfoKey);
			return userPublicInfo;
			
		} catch (EntityNotFoundException e){
			String profileUrl = "/user/" + userId;
			
			userPublicInfo.setProperty("username", user.getEmail());
			userPublicInfo.setProperty("karma", (long) 0);
			userPublicInfo.setProperty("profilePictureUrl", "/_static/img/default-avatar.png");
			userPublicInfo.setProperty("userId", userId);
			userPublicInfo.setProperty("profileUrl", profileUrl);
			userPublicInfo.setProperty("email", user.getEmail());
			
			datastoreService.put(userPublicInfo);
			
			return userPublicInfo;
		}
	}

	public static void updateUserPublicInfo(User user, String property, String newValue) {
		Entity userPublicInfo = getOrCreateUserPublicInfo(user);
		
		userPublicInfo.setProperty(property, newValue);
		
		datastoreService.put(userPublicInfo);
	}
	
	public static Entity getOrCreateUserPrivateInfo(User user){
		
		if (user == null) return null;
		
		String userId = user.getUserId();
		
		Key publicInfoKey = KeyFactory.createKey("UserPrivateInfo", userId);
		
		Entity userPrivateInfo = new Entity(publicInfoKey);
		
		try{
			userPrivateInfo = datastoreService.get(publicInfoKey);
			return userPrivateInfo;
		} catch (EntityNotFoundException e){
			userPrivateInfo.setProperty("emailKarma", "none");
			userPrivateInfo.setProperty("emailRecommend", "weekly");
			userPrivateInfo.setProperty("emailReply", "none");
			
			datastoreService.put(userPrivateInfo);
			return userPrivateInfo;
		}
	}
	
	public static void updateUserPrivateInfo(User user, String property, String newValue) {
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(user);
		
		userPrivateInfo.setProperty(property, newValue);
		
		datastoreService.put(userPrivateInfo);
	}
	
	public static void updateUserPrivateInfo(User user, Map<String, String> properties) {
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(user);
		
		for (String property : properties.keySet()){
			String newValue = properties.get(property);
			userPrivateInfo.setProperty(property, newValue);
		}
		
		datastoreService.put(userPrivateInfo);
	}
}
