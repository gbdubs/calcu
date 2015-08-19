package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.Content;
import calculus.models.Question;
import calculus.models.Topic;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class QuestionAPI {
	
	private static Filter questionFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "question");
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static Question getAnswerableQuestion(String userId) {
		Filter notMe = new FilterPredicate("creatorUserId", FilterOperator.NOT_EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(notMe, questionFilter, viewableFilter, submittedFilter);
		Query query = new Query("Content").setFilter(compositeFilter).addSort("creatorUserId").addSort("createdAt", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(query);
		List<String> skipped = UserPrivateInfoAPI.getUserSkippedContentUuids(userId);
		List<String> answered= UserPrivateInfoAPI.getUserAnsweredContentUuids(userId);
		for (Entity result : pq.asIterable()) {
			String uuid = (String) result.getProperty("uuid");
			if (!skipped.contains(uuid) && !answered.contains(uuid)){
				return new Question(result);
			}
		}
		return null;
	}
	
	public static List<Question> getAllQuestions() {
		List<Content> content = ContentAPI.getAllContentOfType("question");
		List<Question> questions = new ArrayList<Question>();
		for(Content c : content){
			if (c.getContentType().equals("question")){
				Question q = (Question) c;
				questions.add(q);
			}
		}
		return questions;
	}
	
	public static Question getQuestionWithAproximateDifficulty(int difficulty){
		while (difficulty > 0){
			Question q = attemptToGetQuestionWithDifficulty(difficulty);
			if (q != null){
				return q;
			}
			difficulty -= 1000;
		}
		return getQuestionWithAproximateDifficulty(12000);
	}
	
	private static Question attemptToGetQuestionWithDifficulty(int difficulty){
		Topic t = TopicAPI.getTopicWithAproximateDifficulty(difficulty);
		List<String> uuids = t.getContentUuids();
		for (String uuid : uuids){
			try {
				Content c = ContentAPI.instantiateContent(uuid);
				if (c.getContentType().equals("question")){
					return (Question) c;
				}
			} catch (EntityNotFoundException e) {}
		}
		return null;
	}
	
}
