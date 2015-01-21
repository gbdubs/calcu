package calculus.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.PracticeProblem;

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
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class PracticeProblemAPI {
	
	private static Filter practiceProblemFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "practiceProblem");
	private static Filter answerFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "answer");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter unsubmittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, false);
	private static Filter anonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, true);
	private static Filter notAnonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, false);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	private static Filter notViewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, false);
	
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
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, practiceProblemFilter, unsubmittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
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
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, practiceProblemFilter, viewableFilter, submittedFilter, notAnonymousFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}
	
	public static void createOrUpdatePracticeProblemFromRequest(HttpServletRequest req) {
		if (req.getParameter("uuid") == "" || req.getParameter("uuid") == null){
			PracticeProblemAPI.newPracticeProblemFromRequest(req);
		} else {
			PracticeProblemAPI.updatePracticeProblemFromRequest(req);
		}
	}

	public static PracticeProblem newPracticeProblemFromRequest(HttpServletRequest req) {
	
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("anonymousSubmit") != null);
		boolean submitted = (req.getParameter("saveWork") == null);
		boolean viewable = (req.getParameter("saveWork") == null);
		
		String title = (String) req.getParameter("title");
		if (title == null || title == "") title = "[Un-named Problem]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[The biggest problem of all: nothing]";
		Text wrappedBody = new Text(body);
		String authorSolution = (String) req.getParameter("authorSolution");
		if (authorSolution == null || authorSolution == "") authorSolution = "[The Author has not provided an answer to this problem]";
		Text wrappedAuthorSolution = new Text(authorSolution);
		String[] tags = req.getParameter("tagsInput").split(",");
			
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setProperty("uuid", uuid);
		entity.setProperty("contentType", "practiceProblem");
		entity.setProperty("creatorUserId", UserServiceFactory.getUserService().getCurrentUser().getUserId());
		entity.setProperty("createdAt", time);
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("authorSolution", wrappedAuthorSolution);
		entity.setProperty("otherSolutions", "<solutions></solutions>");
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", "/practice-problem/" + uuid);
		entity.setProperty("tags", tags);
		
		datastore.put(entity);
		
		return new PracticeProblem(entity);
	}

	public static PracticeProblem updatePracticeProblemFromRequest(HttpServletRequest req) {
		String uuid = (String) req.getParameter("uuid");
		PracticeProblem pp = new PracticeProblem(uuid);
		Entity entity = pp.getEntity();
		
		long dateAndTime = System.currentTimeMillis();
	
		boolean anonymous = (req.getParameter("anonymousSubmit") != null);
		boolean submitted = (req.getParameter("saveWork") == null);
		boolean viewable = (req.getParameter("saveWork") == null);
		
		String title = (String) req.getParameter("title");
		if (title == null || title == "") title = "[Un-named Problem]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[The biggest problem of all: nothing]";
		Text wrappedBody = new Text(body);
		String authorSolution = (String) req.getParameter("authorSolution");
		if (authorSolution == null || authorSolution == "") authorSolution = "[The Author has not provided an answer to this problem]";
		Text wrappedAuthorSolution = new Text(authorSolution);
	
		entity.setProperty("createdAt", dateAndTime);
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("authorSolution", wrappedAuthorSolution);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		
		datastore.put(entity);
		
		return pp;
	}

	public static List<Answer> getAnswersForPracticeProblem(PracticeProblem pp){
		List<Answer> list = new ArrayList<Answer>();
		String ppUuid = pp.getUuid();
		Filter problemFilter = new FilterPredicate("parentUuid", FilterOperator.EQUAL, ppUuid);
		Filter compositeFilter = CompositeFilterOperator.and(problemFilter, answerFilter, viewableFilter, submittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("approved", SortDirection.DESCENDING).addSort("karma");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Answer a = new Answer(result);
			list.add(a);
		}
		return list;	
	}	
}
