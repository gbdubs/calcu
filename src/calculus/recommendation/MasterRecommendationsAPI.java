package calculus.recommendation;

import java.util.Arrays;
import java.util.Map;

import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;

public class MasterRecommendationsAPI {
	
	public static Map<String, Boolean> getUserInterestPossibilities(String userId) {
		return InterestsAPI.getPotentialAndExistingInterests(userId, 24);
	}

	public static PracticeProblem getDifficultyCalibrationPracticeProblem(String userId) {
		return PracticeProblemAPI.getAnswerablePracticeProblem(userId);
	}
	
	public static Question getDifficultyCalibrationQuestion(String userId) {
		return QuestionAPI.getAnswerableQuestion(userId);
	}

	public static void addDifficultyInformation(String userId, String contentUuid, String difficulty) {
		System.out.println("Content ["+contentUuid+"] was rated ["+difficulty+"] by ["+userId+"].");
	}

	public static void andComparisonInformation(String userId, String preference, String[] allUuids) {
		System.out.println("User ["+userId+"] chose content ["+preference+"] out of the possibilities ["+Arrays.toString(allUuids)+"].");
		
	}

}
