public class ModifiedRatingsAPI {
	public static void submitRating(String contentUuid, String userId, int helpfulness, int difficulty, int quality) {
		//System.out.println("Rating Submitted for ["+contentUuid+"] by ["+userId+"]: H=["+helpfulness+"], D=["+difficulty+"], Q=["+quality+"]");
		
		// Updates our Rating Profiles
		updateRatingProfilesFromRating(userId, contentUuid, helpfulness, difficulty, quality);	
	}
	
	private static void updateRatingProfilesFromRating(String userId, String contentUuid, int reviewedHelpfulness, int reviewedDifficulty, int reviewedQuality){
		Entity userEntity = User.getOrCreateUserRatingProfile(userId);
		Entity contentEntity = Problem.getOrCreateContentRatingProfile(contentUuid);
		double difficultyRating = ((Long) contentEntity.getProperty("difficultyRating")).doubleValue();
		double userAverageDifficulty = ((Long) userEntity.getProperty("averageDifficulty")).doubleValue();
		double userStrength = ((Long) userEntity.getProperty("userStrength")).doubleValue();
		
		// Updates the sorted metrics, UserStrength and Difficulty Rating	
		double expectedDifficulty = (difficultyRating - userStrength);
		double observedDifficulty = (reviewedDifficulty - userAverageDifficulty);
		double difficultyDifferential = observedDifficulty - expectedDifficulty;
		
		int numberOfUserRatings = ((Long) userEntity.getProperty("numRatings")).intValue();
		int numberOfContentRatings = ((Long) contentEntity.getProperty("numRatings")).intValue();
		
		double reviewerTimeBias = reviewerTimeBias(numberOfContentRatings);
		double difficultyTimeBias = difficultyTimeBias(numberOfUserRatings);
		double strengthTimeBias = strengthTimeBias(numberOfUserRatings);
	
		double newUserStrength = userStrength - difficultyDifferential * strengthTimeBias;
		double newDifficultyRating = difficultyRating + difficultyDifferential * reviewerTimeBias * difficultyTimeBias;
		
		userEntity.setProperty("userStrength", ((Double) newUserStrength).longValue());
		contentEntity.setProperty("difficultyRating", ((Double) newDifficultyRating).longValue());
		
		// Updates the counts
		userEntity.setProperty("numRatings", numberOfUserRatings + 1);
		contentEntity.setProperty("numRatings", numberOfContentRatings + 1);
		
		// Updates the simple averages
		updateEntityAverageProperty(userEntity, "averageDifficulty", reviewedDifficulty);
		updateEntityAverageProperty(userEntity, "averageHelpfulness", reviewedHelpfulness);
		updateEntityAverageProperty(userEntity, "averageQuality", reviewedQuality);
		
		updateEntityAverageProperty(contentEntity, "averageDifficulty", reviewedDifficulty);
		updateEntityAverageProperty(contentEntity, "averageHelpfulness", reviewedHelpfulness);
		updateEntityAverageProperty(contentEntity, "averageQuality", reviewedQuality);	
	}
	
	private static void updateEntityAverageProperty(Entity e, String prop, int rating){
		double oldValue = ((Long) e.getProperty(prop)).doubleValue();
		int n = ((Long) e.getProperty("numRatings")).intValue();
		Double newValue = ((Double) (oldValue * n + rating * 10) / (n + 1));
		e.setProperty(prop, newValue.longValue());
	}
	
	private static double reviewerTimeBias(int numberOfContentRatings) {
		return 1 + 4.0 / (1 + numberOfContentRatings);
	}

	private static double strengthTimeBias(int numberOfRatings){
		return (2 / ( 1 + Math.pow(1.5, (-0.1 * (8 - numberOfRatings)))) + 0.5 ) / 1.7;
	}

	private static double difficultyTimeBias(int numberOfRatings){
		return 1 / (1 + Math.pow(2, (6.0 - numberOfRatings)));
	}
}
