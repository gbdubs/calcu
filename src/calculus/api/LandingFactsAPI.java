package calculus.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class LandingFactsAPI {

	private static String stringKey = "023jnlsel9ur2039julkwsej";
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static Entity getOrRefreshLandingEntity(){
		Entity entity = getLandingEntity();
		Long createdAt = (Long) entity.getProperty("updatedAt");
		if (createdAt == null || System.currentTimeMillis() - createdAt > 1000){
			int content = calcNumPiecesOfContent();
			int users = calcNumUsers();
			int recentPosts = calcNumRecentPosts();
			
			entity.setUnindexedProperty("numPiecesOfContent", new Long(content));
			entity.setUnindexedProperty("numRecentPosts", new Long(recentPosts));
			entity.setUnindexedProperty("numUsers", new Long(users));
			entity.setUnindexedProperty("updatedAt", System.currentTimeMillis());
			
			datastore.put(entity);
		}
		return entity;
	}

	public static int getNumUsers(Entity entity) {
		Long numUsers = (Long) entity.getProperty("numUsers");
		if (numUsers == null) return 0;
		return numUsers.intValue();
	}

	public static int getNumRecentPosts(Entity entity) {
		Long numRecentPosts = (Long) entity.getProperty("numRecentPosts");
		if (numRecentPosts == null) return 0;
		return numRecentPosts.intValue();
	}

	public static int getNumPiecesOfContent(Entity entity) {
		Long numPoC = (Long) entity.getProperty("numPiecesOfContent");
		if (numPoC == null) return 0;
		return numPoC.intValue();
	}

	private static Entity getLandingEntity() {
		Key key = KeyFactory.createKey("LandingFacts", stringKey);
		Entity entity = new Entity(key);
		try {
			entity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			
		}
		return entity;
	}

	private static int calcNumRecentPosts() {
		// Defines the threshold to be one week ago exactly.
		Long threshold = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7;
		Filter filter = new FilterPredicate("createdAt", FilterOperator.GREATER_THAN, threshold);
		Query q = new Query("Content").setFilter(filter).setKeysOnly();
		PreparedQuery pq = datastore.prepare(q);
		return pq.countEntities(FetchOptions.Builder.withDefaults());
	}

	private static int calcNumUsers() {
		Query query = new Query("__Stat_Kind__");
		query.setFilter(new FilterPredicate("kind_name", FilterOperator.EQUAL, "UserPublicInfo"));    
		Entity entity = datastore.prepare(query).asSingleEntity();
		if (entity == null) return -1;
		Long l = (Long) entity.getProperty("count");
		if (l == null) return 0;
		return l.intValue();
	}

	private static int calcNumPiecesOfContent() {
		Query query = new Query("__Stat_Kind__");
		query.setFilter(new FilterPredicate("kind_name", FilterOperator.EQUAL, "Content"));    
		Entity entity = datastore.prepare(query).asSingleEntity();
		if (entity == null) return -1;
		Long l = (Long) entity.getProperty("count");
		if (l == null) return 0;
		return l.intValue();
	}
}
