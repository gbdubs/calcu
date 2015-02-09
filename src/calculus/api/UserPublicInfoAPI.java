package calculus.api;

import calculus.recommendation.PhenotypeAPI;
import calculus.utilities.UrlGenerator;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
			
			userPublicInfo.setUnindexedProperty("username", user.getEmail());
			userPublicInfo.setProperty("karma", (long) 0);
			userPublicInfo.setUnindexedProperty("level", (long) 1);
			userPublicInfo.setUnindexedProperty("profilePictureUrl", "/_static/img/default-avatar.png");
			userPublicInfo.setUnindexedProperty("userId", userId);
			userPublicInfo.setUnindexedProperty("profileUrl", profileUrl);
			userPublicInfo.setUnindexedProperty("email", user.getEmail());
			userPublicInfo.setUnindexedProperty("phenotype", PhenotypeAPI.DEFAULT_PHENOTYPE);
			
			datastore.put(userPublicInfo);
			
			return userPublicInfo;
		}
	}

	public static Entity getUserPublicInfo(String userId){
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", userId);
		try {
			return datastore.get(publicInfoKey);
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Attempted to access userPublicInfo that did not exist: " + userId);
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
			
			userPublicInfo.setUnindexedProperty("username", "Anonymous Elephant");
			userPublicInfo.setProperty("karma", (long) 0);
			userPublicInfo.setUnindexedProperty("level", (long) 1);
			userPublicInfo.setUnindexedProperty("profilePictureUrl", "/_static/img/elephant.png");
			userPublicInfo.setUnindexedProperty("userId", userId);
			userPublicInfo.setUnindexedProperty("profileUrl", profileUrl);
			userPublicInfo.setUnindexedProperty("email", "jumbodumbo@gmail.com");
			userPublicInfo.setUnindexedProperty("phenotype", PhenotypeAPI.DEFAULT_PHENOTYPE);
			return userPublicInfo;
		}
	}

	public static void updateUsername(String username) {
		Entity userPublicInfo = getOrCreateMyPublicInfo();
		userPublicInfo.setUnindexedProperty("username", username);
		datastore.put(userPublicInfo);
	}

	public static void setUserProfilePictureServingUrl(String servingUrl) {
		Entity userPublicInfo = getOrCreateMyPublicInfo();
		userPublicInfo.setUnindexedProperty("profilePictureUrl", servingUrl);
		datastore.put(userPublicInfo);
	}

	public static BlobKey getProfilePictureBlobKey() {
		Entity userPublicInfo = getOrCreateMyPublicInfo();
		return (BlobKey) userPublicInfo.getProperty("profilePictureBlobKey");
	}
	
	public static void setProfilePictureBlobKey(BlobKey blobKey){
		Entity userPublicInfo = getOrCreateMyPublicInfo();
		userPublicInfo.setUnindexedProperty("profilePictureBlobKey", blobKey);
		datastore.put(userPublicInfo);
	}

	public static String getProfilePictureUrl(String associatedUserId) {
		Entity userPublicInfo = getOrCreateUserPublicInfo(associatedUserId);
		return (String) userPublicInfo.getProperty("profilePictureUrl");
	}

	public static String getUsername(String userId) {
		Entity userPublicInfo = getUserPublicInfo(userId);
		if (userPublicInfo == null) return "";
		return (String) userPublicInfo.getProperty("username");
	}
	
	public static String getPhenotype(String userId){
		Entity userPublicInfo = getUserPublicInfo(userId);
		if (userPublicInfo == null) return "";
		return (String) userPublicInfo.getProperty("phenotype");
	}

	public static void setUserPhenotype(String userId, String newPhenotype) {
		Entity userPublicInfo = getUserPublicInfo(userId);
		userPublicInfo.setUnindexedProperty("phenotype", newPhenotype);
		datastore.put(userPublicInfo);
	}
}
