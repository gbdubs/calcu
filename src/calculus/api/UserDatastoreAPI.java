package calculus.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import calculus.utilities.UrlGenerator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.users.User;

public class UserDatastoreAPI {

	private static Set<String> PRIVATE_FIELDS;
	
	static{
		PRIVATE_FIELDS = new HashSet<String>();
		PRIVATE_FIELDS.add("emailKarma");
		PRIVATE_FIELDS.add("emailRecommend");
		PRIVATE_FIELDS.add("emailReply");
		PRIVATE_FIELDS.add("bookmarks");
	}
	
	private static DatastoreService datastore= DatastoreServiceFactory.getDatastoreService();
	
	public static Entity getUserPublicInfo(String userId){
		Key publicInfoKey = KeyFactory.createKey("userPublicInfo", userId);
		try {
			return datastore.get(publicInfoKey);
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Attempted to access userPublicInfo that did not exist.");
		}
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
	
	public static Entity getOrCreateUserPublicInfo(String userId){
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", userId);
		
		Entity userPublicInfo = new Entity(publicInfoKey);
		
		try{
			userPublicInfo = datastore.get(publicInfoKey);
			return userPublicInfo;
			
		} catch (EntityNotFoundException e){
			String profileUrl = UrlGenerator.profileUrl(userId);
			
			userPublicInfo.setProperty("username", "Anonymous Anteater");
			userPublicInfo.setProperty("karma", (long) 0);
			userPublicInfo.setProperty("profilePictureUrl", "/_static/img/default-avatar.png");
			userPublicInfo.setProperty("userId", userId);
			userPublicInfo.setProperty("profileUrl", profileUrl);
			userPublicInfo.setProperty("email", "AntLover444@gmail.com");
			
			datastore.put(userPublicInfo);
			
			return userPublicInfo;
		}
	}
	
	public static Entity getOrCreateUserPrivateInfo(User user){
		if (user == null) return null;
		return getOrCreateUserPrivateInfo(user.getUserId());
	}

	public static Entity getOrCreateUserPrivateInfo(String userId) {
		Key publicInfoKey = KeyFactory.createKey("UserPrivateInfo", userId);
		
		Entity userPrivateInfo = new Entity(publicInfoKey);
		
		try{
			userPrivateInfo = datastore.get(publicInfoKey);
			return userPrivateInfo;
		} catch (EntityNotFoundException e){
			List<String> bookmarks = new ArrayList<String>();
			userPrivateInfo.setProperty("emailKarma", "none");
			userPrivateInfo.setProperty("emailRecommend", "weekly");
			userPrivateInfo.setProperty("emailReply", "none");
			userPrivateInfo.setProperty("bookmarks", bookmarks);
			System.out.println(userPrivateInfo.toString());
			datastore.put(userPrivateInfo);
			return userPrivateInfo;
		}
	}

	public static void setUserEmailPreferences(User user, Map<String, String> preferences) {
		Entity userPrivateInfo = getOrCreateUserPrivateInfo(user);
		for (String property : preferences.keySet()){
			if (PRIVATE_FIELDS.contains(property)){
				userPrivateInfo.setProperty(property, preferences.get(property));
			} else {
				System.out.println("USER DATASTORE API ERROR: Unknown Property");
			}
		}
	}

	public static void updateUsername(User user, String username) {
		
		Entity userPublicInfo = UserDatastoreAPI.getOrCreateMyPublicInfo(user);
		
		userPublicInfo.setProperty("username", username);
		
		datastore.put(userPublicInfo);
	}

	public static void setUserProfilePicture(Image perfectImage) {
		// TODO Auto-generated method stub
		
	}

}
