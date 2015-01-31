package calculus.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import calculus.models.PracticeProblem;
import calculus.models.Question;

public class RecommendationsAPI {

	public static boolean[] temp = new boolean[24];
	
	public static Map<String, Boolean> getUserInterestPossibilities(String userId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		for(int i = 0; i < 24; i++){
			result.put("" + i, temp[i]);
		}
		
		return result;
	}

	public static void addInterest(String interest, String userId) {
		int i = Integer.parseInt(interest);
		temp[i] = true;
		
	}

	public static void removeInterest(String interest, String userId) {
		int i = Integer.parseInt(interest);
		temp[i] = false;
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
