package calculus.api;

import calculus.models.Question;
import calculus.recommendation.SkillsAPI;

public class BaselineAPI {
	
	/**
	 * 
	 * @param originalDifficulty ORIGINAL VALUE, 0 to MAX_TOPIC_DIFFICULTY
	 * @param rating             A VALUE FROM 0 TO 10
	 * @param round              A VALUE FROM 0 TO 10
	 * @return
	 */
	public static int transformDifficulty(int originalDifficulty, int rating, int round){
		int toChange = (rating - 5) * Math.abs(rating - 5) * (10 - round) * 1000;
		return originalDifficulty + toChange;
	}
	
	public static String getQuestionForBaseliningUser(int difficulty){
		Question q = QuestionAPI.getQuestionWithAproximateDifficulty(difficulty);
		return q.getUuid();
	}

	public static void userRankedProblemWithDifficulty(String userId, String problemUuid, double difficulty) {
		float difficultyRating = (float) difficulty;
		SkillsAPI.userRatedContentThisDifficulty(userId, problemUuid, difficultyRating);
	}
	
}
