package calculus.api;

import calculus.models.Content;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class KarmaAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static void incrementUserKarma(String userId, int differential) {
		Entity userPublicInfo = UserPublicInfoAPI.getOrCreateUserPublicInfo(userId);
		long karma = (long) userPublicInfo.getProperty("karma");
		userPublicInfo.setProperty("karma", karma + differential);
		datastore.put(userPublicInfo);
	}
	
	public static void incrementContentKarma(String contentUuid, int differential){
		try {
			Content c = new Content(contentUuid);
			c.incrementKarma(differential);
			String authorId = c.getAuthor().getUserId();
			incrementUserKarma(authorId, differential);
		} catch (EntityNotFoundException e) {
			// Don't worry aobut it, and dont' increment any karma!
		}
	}
}