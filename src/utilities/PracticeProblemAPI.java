package utilities;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;

public class PracticeProblemAPI {
	
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, "true");
	private static Filter unsubmittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, "false");
	private static Filter anonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, "true");
	private static Filter notAnonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, "false");
	private static Filter visibleFilter = new FilterPredicate("visible", FilterOperator.EQUAL, "true");
	private static Filter invisibleFilter = new FilterPredicate("visible", FilterOperator.EQUAL, "false");
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void addPracticeProblemContext(HttpServletRequest req, String uuid){
		PracticeProblem practiceProblem = new PracticeProblem(uuid);
		req.setAttribute("practiceProblem", practiceProblem);
	}
	
	public static void addPracticeProblemContext(HttpServletRequest req, PracticeProblem practiceProblem){
		req.setAttribute("practiceProblem", practiceProblem);
	}
	
	public static void addPracticeProblemEditorContext(HttpServletRequest req, String uuid){
		PracticeProblem practiceProblem = new PracticeProblem(uuid);
		req.setAttribute("practiceProblem", practiceProblem);
	}
	
	public static void addPracticeProblemEditorContext(HttpServletRequest req, PracticeProblem practiceProblem){		
		req.setAttribute("practiceProblem", practiceProblem);
	}
	
	public static List<PracticeProblem> getUnsubmittedPracticeProblems(User user){
		List<PracticeProblem> list = new ArrayList<PracticeProblem>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, user.getUserId());
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, unsubmittedFilter);
		Query query = new Query("PracticeProblem").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}
	
	public static List<PracticeProblem> getSubmittedPracticeProblems(User user){
		List<PracticeProblem> list = new ArrayList<PracticeProblem>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, user.getUserId());
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, visibleFilter, submittedFilter, notAnonymousFilter);
		Query query = new Query("PracticeProblem").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}
	
	public static List<PracticeProblem> getAllCreatedPracticeProblems(User user){
		List<PracticeProblem> list = new ArrayList<PracticeProblem>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, user.getUserId());
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, visibleFilter);
		Query query = new Query("PracticeProblem").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}
	
	public static PracticeProblem storeNewPracticeProblemFromRequest(HttpServletRequest req){
		return PracticeProblem.newPracticeProblemFromRequest(req);
	}
	
}
