package calculus.api;

import calculus.models.Question;
import calculus.recommendation.SkillsAPI;

public class BaselineAPI {

	public static String getQuestionForBaseliningUser(String userId){
		Question q = QuestionAPI.getAnswerableQuestion(userId);
		UserPrivateInfoAPI.addUserSkippedContent(userId, q.getUuid());
		return q.getUuid();
	}

	public static void userRankedProblemWithDifficulty(String userId, String problemUuid, double difficulty) {
		float difficultyRating = (float) difficulty;
		SkillsAPI.userRatedContentThisDifficulty(userId, problemUuid, difficultyRating);
	}
	
}
