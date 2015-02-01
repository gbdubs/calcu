package calculus.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class RatingsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void submitRating(String contentUuid, String userId, int helpfulness, int difficulty, int quality) {
		System.out.println("Rating Submitted for ["+contentUuid+"] by ["+userId+"]: H=["+helpfulness+"], D=["+difficulty+"], Q=["+quality+"]");
		
		// Updates our Rating Profiles
		updateRatingProfilesFromRating(userId, contentUuid, helpfulness, difficulty, quality);
		
		KarmaAPI.updateKarmaFromRating(contentUuid, helpfulness, difficulty, quality);
	}
	
	private static void updateRatingProfilesFromRating(String userId, String contentUuid, int reviewedHelpfulness, int reviewedDifficulty, int reviewedQuality){
		Entity userEntity = getOrCreateUserRatingProfile(userId);
		Entity contentEntity = getOrCreateContentRatingProfile(contentUuid);
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
		
		userEntity.setProperty("userStrength", newUserStrength);
		contentEntity.setProperty("difficultyRating", newDifficultyRating);
		
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
		
		// Saves the changes
		datastore.put(userEntity);
		datastore.put(contentEntity);
	}
	
	private static void updateEntityAverageProperty(Entity e, String prop, int rating){
		double oldValue = (long) e.getProperty(prop);
		int n = ((Long) e.getProperty("numRatings")).intValue();
		double newValue = (oldValue * n + rating * 10) / (n + 1);
		e.setProperty(prop, newValue);
	}
	
	private static Entity getOrCreateContentRatingProfile(String contentUuid) {
		Key userKey = KeyFactory.createKey("ContentRatingProfile", contentUuid);
		try {
			return datastore.get(userKey);
		} catch (EntityNotFoundException e) {
			Entity result = new Entity(userKey);
			// NumRatings allows us to calculate averages, while not maintaining
			// references to individual ratings.
			result.setProperty("numRatings", 0);
			
			// Average ones are use to gauge reactions against
			result.setProperty("averageDifficulty", 500);
			result.setProperty("averageQuality", 500);
			result.setProperty("averageHelpfulness", 500);
			
			// Perceived strength tells us how difficult they like their questions
			result.setProperty("difficultyRating", 500);
			return result;
		}
	}

	private static Entity getOrCreateUserRatingProfile(String userId){
		Key userKey = KeyFactory.createKey("UserRatingProfile", userId);
		try {
			return datastore.get(userKey);
		} catch (EntityNotFoundException e) {
			Entity result = new Entity(userKey);
			// NumRatings allows us to calculate averages, while not maintaining
			// references to individual ratings.
			result.setProperty("numRatings", 0);
			
			// Average ones are use to gauge reactions against
			result.setProperty("averageDifficulty", 500);
			result.setProperty("averageQuality", 500);
			result.setProperty("averageHelpfulness", 500);
			
			// Perceived strength tells us how difficult they like their questions
			result.setProperty("userStrength", 500);
			
			return result;
		}
	}

	private static double reviewerTimeBias(int numberOfContentRatings) {
		return 1 + 4.0 / numberOfContentRatings;
	}

	private static double strengthTimeBias(int numberOfRatings){
		return (2 / ( 1 + Math.pow(1.5, (-0.1 * (8 - numberOfRatings)))) + 0.5 ) / 1.7;
	}

	private static double difficultyTimeBias(int numberOfRatings){
		return 1 / (1 + Math.pow(2, (6.0 - numberOfRatings)));
	}
}
