package calculus.api;

import java.util.List;

import calculus.models.PracticeProblem;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PracticeProblemAPI {
	
	private static Filter practiceProblemFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "practiceProblem");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static PracticeProblem getAnswerablePracticeProblem(String userId) {
		Filter notMe = new FilterPredicate("creatorUserId", FilterOperator.NOT_EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(notMe, practiceProblemFilter, viewableFilter, submittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("creatorUserId").addSort("createdAt", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(query);
		List<String> skipped = UserPrivateInfoAPI.getUserSkippedContentUuids(userId);
		List<String> answered= UserPrivateInfoAPI.getUserAnsweredContentUuids(userId);
		
		for (Entity result : pq.asIterable()) {
			String uuid = (String) result.getProperty("uuid");
			if (!skipped.contains(uuid) && !answered.contains(uuid)){
				return new PracticeProblem(result);
			}
		}
		return null;
	}
}
