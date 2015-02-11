package calculus.contribute;

import java.util.ArrayList;
import java.util.List;

import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class ContributionDashboardAPI {
	
	private static Filter textContentFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "textContent");
	private static Filter practiceProblemFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "practiceProblem");
	private static Filter questionFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "question");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter unsubmittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, false);
	private static Filter notAnonymousFilter = new FilterPredicate("anonymous", FilterOperator.EQUAL, false);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<PracticeProblem> getUnsubmittedPracticeProblems(String userId){
		List<PracticeProblem> list = new ArrayList<PracticeProblem>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, practiceProblemFilter, unsubmittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}

	public static List<PracticeProblem> getSubmittedPracticeProblems(String userId){
		List<PracticeProblem> list = new ArrayList<PracticeProblem>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, practiceProblemFilter, viewableFilter, submittedFilter, notAnonymousFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			PracticeProblem pp = new PracticeProblem(result);
			list.add(pp);
		}
		return list;
	}

	public static List<Question> getUnsubmittedQuestions(String userId) {
		List<Question> list = new ArrayList<Question>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, questionFilter, unsubmittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Question q = new Question(result);
			list.add(q);
		}
		return list;
	}

	public static List<Question> getSubmittedQuestions(String userId) {
		List<Question> list = new ArrayList<Question>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, questionFilter, submittedFilter, notAnonymousFilter, viewableFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Question q = new Question(result);
			list.add(q);
		}
		return list;
	}

	public static List<TextContent> getUnsubmittedTextContent(String userId) {
		List<TextContent> list = new ArrayList<TextContent>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, textContentFilter, unsubmittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			TextContent q = new TextContent(result);
			list.add(q);
		}
		return list;
	}

	public static List<TextContent> getSubmittedTextContent(String userId) {
		List<TextContent> list = new ArrayList<TextContent>();
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(userFilter, textContentFilter, submittedFilter, notAnonymousFilter, viewableFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			TextContent q = new TextContent(result);
			list.add(q);
		}
		return list;
	}

}
