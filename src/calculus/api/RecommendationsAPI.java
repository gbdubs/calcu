package calculus.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculus.models.PracticeProblem;
import calculus.models.Question;

public class RecommendationsAPI {
	
	public static Map<String, Boolean> getUserInterestPossibilities(String userId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		int maxResults = 24;
		List<String> newInterests = InterestsAPI.getPotentialInterests(userId, maxResults);
		System.out.println("newInterests:" + newInterests);
		List<String> oldInterests = InterestsAPI.getAndCycleFirstNInterests(userId, Math.max(maxResults - newInterests.size(), 4));
		
		System.out.println("oldINterests:" + oldInterests.toString());
		for (String oi : oldInterests){
			result.put(oi, true);
		}
		int i = 0;
		while (result.size() < maxResults && i < newInterests.size()){
			result.put(newInterests.get(i), false);
			i++;
		}

		return result;
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
