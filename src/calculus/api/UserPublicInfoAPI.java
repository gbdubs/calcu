package calculus.api;

import calculus.utilities.UrlGenerator;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class UserPublicInfoAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	private static Entity getOrCreateMyPublicInfo() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) return null;
		return getOrCreateMyPublicInfo(user);
	}
	
	public static Entity getOrCreateMyPublicInfo(User user){
		if (user == null) return null;
		
		String userId = user.getUserId();
		
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", userId);
		
		Entity userPublicInfo = new Entity(publicInfoKey);
		
		try{
			userPublicInfo = datastore.get(publicInfoKey);
			return userPublicInfo;
			
		} catch (EntityNotFoundException e){
			String profileUrl = "/user/" + userId;
			
			userPublicInfo.setProperty("username", user.getEmail());
			userPublicInfo.setProperty("karma", (long) 0);
			userPublicInfo.setProperty("profilePictureUrl", "/_static/img/default-avatar.png");
			userPublicInfo.setProperty("userId", userId);
			userPublicInfo.setProperty("profileUrl", profileUrl);
			userPublicInfo.setProperty("email", user.getEmail());
			
			datastore.put(userPublicInfo);
			
			return userPublicInfo;
		}
	}

	public static Entity getUserPublicInfo(String userId){
		Key publicInfoKey = KeyFactory.createKey("userPublicInfo", userId);
		try {
			return datastore.get(publicInfoKey);
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Attempted to access userPublicInfo that did not exist.");
		}
	}
	
	public static Entity getOrCreateUserPublicInfo(String userId){
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", userId);
		
		Entity userPublicInfo = new Entity(publicInfoKey);
		
		try{
			userPublicInfo = datastore.get(publicInfoKey);
			return userPublicInfo;
			
		} catch (EntityNotFoundException e){
			String profileUrl = UrlGenerator.profileUrl(userId);
			
			userPublicInfo.setProperty("username", "Anonymous Elephant");
			userPublicInfo.setProperty("karma", (long) 0);
			userPublicInfo.setProperty("profilePictureUrl", "/_static/img/elephant.png");
			userPublicInfo.setProperty("userId", userId);
			userPublicInfo.setProperty("profileUrl", profileUrl);
			userPublicInfo.setProperty("email", "jumbodumbo@gmail.com");
			
			return userPublicInfo;
		}
	}

	public static void updateUsername(String username) {
		Entity userPublicInfo = getOrCreateMyPublicInfo();
		userPublicInfo.setProperty("username", username);
		datastore.put(userPublicInfo);
	}

	public static void setUserProfilePictureServingUrl(String servingUrl) {
		Entity userPublicInfo = getOrCreateMyPublicInfo();
		userPublicInfo.setProperty("profilePictureUrl", servingUrl);
		datastore.put(userPublicInfo);
	}
}
