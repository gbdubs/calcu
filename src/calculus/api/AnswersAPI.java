package calculus.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import calculus.models.Answer;
import calculus.models.Content;

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
	
	public static boolean userHasAnsweredContent(String userId, String uuid) {
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

	public static List<Answer> getAllAnswers() {
		List<Content> content = ContentAPI.getAllContentOfType("answer");
		List<Answer> answers = new ArrayList<Answer>();
		for(Content c : content){
			if (c.getContentType().equals("answer")){
				Answer a = (Answer) c;
				answers.add(a);
			}
		}
		return answers;
	}
}
