package utilities;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

public class UserInitializer {

	private static volatile int userCount = 0;
	
	public static void initializeUserPublicInfo(User user){
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Key publicInfoKey = KeyFactory.createKey("UserPublicInfo", user.getUserId());
		
		Entity userPublicInfo = new Entity(publicInfoKey);
		
		try{
			userPublicInfo = ds.get(publicInfoKey);
			return;
		} catch (EntityNotFoundException e){
			
		}
		
		int myPublicKey = 0;
		synchronized(UserInitializer.class){
			myPublicKey = userCount++;
		}
		
		String profileUrl = "/user/" + myPublicKey;
		
		userPublicInfo.setProperty("username", user.getEmail());
		userPublicInfo.setProperty("karma", 0);
		userPublicInfo.setProperty("profilePictureUrl", "/_static/img/default-avatar.png");
		userPublicInfo.setProperty("publicId", myPublicKey);
		userPublicInfo.setProperty("profileUrl", profileUrl);
		userPublicInfo.setProperty("email", user.getEmail());
		
		ds.put(userPublicInfo);
	}
	
	
}
