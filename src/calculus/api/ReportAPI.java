package calculus.api;

import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

import calculus.models.Content;

public class ReportAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void fileReport(User user, String contentUuid, String reason){
		
		Content.setInvisible(contentUuid);
		
		String uuid = UUID.randomUUID().toString();
		
		Entity entity = new Entity(KeyFactory.createKey("ReportedContent", uuid));
		
		entity.setProperty("userId", user.getUserId());
		entity.setProperty("contentUuid", contentUuid);
		entity.setProperty("reason", reason);
		entity.setProperty("uuid", uuid);
		
		datastore.put(entity);
	}
	
	// TODO: Fill out the ReportAPI
	// Not Essential for a long time, should be delayed as secondary priority.
	public static void clearReport(String uuid){	
		// Send a message to the reporter
	}
	
	public static void confirmReport(String uuid){
		// Send a message to the User
		// Send a message to the reporter
	}
	
	public static String getReportLog(){
		return null;
	}
	
}
