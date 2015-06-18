package calculus.api;

import calculus.models.Question;

public class BaselineAPI {

	public static String getQuestionForBaseliningUser(String userId){
		// DUMMY
		Question q = QuestionAPI.getAnswerableQuestion(userId);
		return q.getUuid();
	}

	public static void userRankedProblemWithDifficulty(String userId, String problemUuid, int stepNumber) {
		// DUMMY
		
		
	}
	
}
