package utilities;

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
		
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", user.getUserId());
		
		Entity userPublicInfo = new Entity(publicInfoKey);
		
		try{
			userPublicInfo = datastoreService.get(publicInfoKey);
			return userPublicInfo;
		} catch (EntityNotFoundException e){}
		
		String userId = user.getUserId();
		
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

	public static void updateUserPublicInfo(User user, String property, String newValue) {
		Entity userPublicInfo = getOrCreateUserPublicInfo(user);
		
		userPublicInfo.setProperty(property, newValue);
		
		datastoreService.put(userPublicInfo);
	}
}
