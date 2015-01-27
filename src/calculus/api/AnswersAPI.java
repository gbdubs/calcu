package calculus.api;

import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class AnswersAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static boolean userAnsweredContent(String userId, String uuid) {
		Filter answerFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "answer");
		Filter problemFilter = new FilterPredicate("parentUuid", FilterOperator.EQUAL, uuid);
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(problemFilter, userFilter, answerFilter);
		Query query = new Query("Content").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(query);
		for (Iterator<Entity> iterator = pq.asIterable().iterator(); iterator.hasNext();) {
			return true;
		}
		return false;
	}

}
