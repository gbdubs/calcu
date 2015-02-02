package calculus.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class KarmaAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static void incrementUserKarma(String userId, int differential) {
		Entity userPublicInfo = UserPublicInfoAPI.getOrCreateUserPublicInfo(userId);
		long karma = (long) userPublicInfo.getProperty("karma");
		userPublicInfo.setProperty("karma", karma + differential);
		datastore.put(userPublicInfo);
	}
}