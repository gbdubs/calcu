package calculus.recommendation;

import java.util.List;

import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class PublicRecommendationAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void dismissRecommendation(String userId, String contentUuid){
		MasterRecommendationsAPI.removeRecommendation(userId, contentUuid);
	}
	
	public static void resetHiddenRecommendations(String userId){
		MasterRecommendationsAPI.clearHidden(userId);
	}
	
	
	
	public static PracticeProblem getDifficultyCalibrationPracticeProblem(String userId) {
		return PracticeProblemAPI.getAnswerablePracticeProblem(userId);
	}

	public static Question getDifficultyCalibrationQuestion(String userId) {
		return QuestionAPI.getAnswerableQuestion(userId);
	}

	public static void refreshRecommendations(String userId) {
		MasterRecommendationsAPI.refreshRecommendations(userId);
	}

	public static List<String> getUsersInNeedOfRecalculation(int numToCalc) {
		return MasterRecommendationsAPI.getUsersInNeedOfRecalculation(numToCalc);
	}
}
