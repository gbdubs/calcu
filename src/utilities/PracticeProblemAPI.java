package utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
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
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, "true");
	private static Filter notViewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, "false");
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
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, viewableFilter, submittedFilter, notAnonymousFilter);
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
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, viewableFilter);
		Query query = new Query("PracticeProblem").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}
	
	public static void createOrUpdatePracticeProblemFromRequest(HttpServletRequest req) {
		if (req.getParameter("PracticeProblemId") == "" || req.getParameter("PracticeProblemId") == null){
			System.out.println(req.getParameter("PracticeProblemId"));
			PracticeProblemAPI.newPracticeProblemFromRequest(req);
		} else {
			PracticeProblemAPI.updatePracticeProblemFromRequest(req);
		}
	}

	public static PracticeProblem newPracticeProblemFromRequest(HttpServletRequest req) {
	
		String uuid = UUID.randomUUID().toString();
		String dateAndTime = (new Date()).toString();
	
		String anonymous = ((Boolean) (req.getParameter("anonymousSubmit") != null)).toString();
		String submitted = ((Boolean) (req.getParameter("saveWork") == null)).toString();
		String viewable = ((Boolean) (req.getParameter("saveWork") == null)).toString();
		
		String problemTitle = (String) req.getParameter("problemTitle");
		if (problemTitle == null || problemTitle == "") problemTitle = "[Un-named Problem]";
		String problemBody = (String) req.getParameter("problemBody");
		if (problemBody == null || problemBody == "") problemBody = "[The biggest problem of all: nothing]";
		String authorSolution = (String) req.getParameter("authorSolution");
		if (authorSolution == null || authorSolution == "") authorSolution = "[The Author has not provided an answer to this problem]";
		
		
		Entity entity = new Entity(KeyFactory.createKey("PracticeProblem", uuid));
		
		entity.setProperty("practiceProblemId", uuid);
		entity.setProperty("creatorUserId", PracticeProblem.userService.getCurrentUser().getUserId());
		entity.setProperty("createdAt", dateAndTime);
		entity.setProperty("problemTitle", problemTitle);
		entity.setProperty("problemBody", problemBody);
		entity.setProperty("authorSolution", authorSolution);
		entity.setProperty("otherSolutions", "<solutions></solutions>");
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", "/practice-problems/" + uuid);
		
		PracticeProblem.datastoreService.put(entity);
		
		return new PracticeProblem(entity);
	}

	public static PracticeProblem updatePracticeProblemFromRequest(HttpServletRequest req) {
		String uuid = (String) req.getParameter("PracticeProblemId");
		PracticeProblem pp = new PracticeProblem(uuid);
		Entity entity = pp.entity;
		
		String dateAndTime = (new Date()).toString();
	
		String anonymous = ((Boolean) (req.getParameter("anonymousSubmit") != null)).toString();
		String submitted = ((Boolean) (req.getParameter("saveWork") == null)).toString();
		String viewable = ((Boolean) (req.getParameter("saveWork") == null)).toString();
		
		String problemTitle = (String) req.getParameter("problemTitle");
		if (problemTitle == null || problemTitle == "") problemTitle = "[Un-named Problem]";
		String problemBody = (String) req.getParameter("problemBody");
		if (problemBody == null || problemBody == "") problemBody = "[The biggest problem of all: nothing]";
		String authorSolution = (String) req.getParameter("authorSolution");
		if (authorSolution == null || authorSolution == "") authorSolution = "[The Author has not provided an answer to this problem]";
	
		entity.setProperty("createdAt", dateAndTime);
		entity.setProperty("problemTitle", problemTitle);
		entity.setProperty("problemBody", problemBody);
		entity.setProperty("authorSolution", authorSolution);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		
		PracticeProblem.datastoreService.put(entity);
		
		return pp;
	}
}
