package calculus.api;

import java.util.List;

import calculus.recommendation.HelpfulContentAPI;
import calculus.recommendation.InterestsAPI;
import calculus.recommendation.SkillsAPI;
import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class RatingsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void submitRating(String contentUuid, String userId, int helpfulness, int difficulty, int quality) {		
		// Updates our Rating Profiles
		updateRatingProfilesFromRating(userId, contentUuid, helpfulness, difficulty, quality);
		
		updateKarmaFromRating(userId, contentUuid, quality);
		SkillsAPI.userRatedContentThisDifficulty(userId, contentUuid, (float) difficulty / 100);
		InterestsAPI.userRatedContent(userId, contentUuid);
		
		if (helpfulness > 85){
			HelpfulContentAPI.userFoundContentHelpful(userId, contentUuid);
			if (helpfulness == 100 && quality == 100){
				String authorId = ContentAPI.getContentAuthorId(contentUuid);
				AchievementsAPI.loveAchievement(authorId);
			}
		}
	}
	
	private static void updateKarmaFromRating(String raterId, String contentUuid, int quality) {
		if (!testAndSetContentRatedByUser(contentUuid, raterId)){
			int differential = (int) (400.0 * (quality - 30)/70.0);
			Entity content = getOrCreateContentRatingProfile(contentUuid);
			long originalKarma = (long) content.getProperty("karma");
			content.setProperty("karma", originalKarma + differential);
			datastore.put(content);
			int oldKarma = (int) (originalKarma / 100.0);
			int newKarma = (int) ((originalKarma + differential) / 100.0);
			int visibleChange = newKarma - oldKarma;
			KarmaAPI.incrementContentKarma(contentUuid, visibleChange);
			// Give the Rater instant karma for submitting the rating
			KarmaAPI.incrementUserKarmaForRatingOthers(raterId, 1);
		}
	}

	private static void updateRatingProfilesFromRating(String userId, String contentUuid, int reviewedHelpfulness, int reviewedDifficulty, int reviewedQuality){
		Entity userEntity = getOrCreateUserRatingProfile(userId);
		Entity contentEntity = getOrCreateContentRatingProfile(contentUuid);
		
		int difficultyRating = intValue(contentEntity, "difficultyRating");
		int userAverageDifficulty = intValue(userEntity, "averageDifficulty");
		int userStrength = intValue(userEntity,"userStrength");
		
		// Updates the sorted metrics, UserStrength and Difficulty Rating	
		int expectedDifficulty = (difficultyRating - userStrength);
		int observedDifficulty = (reviewedDifficulty - userAverageDifficulty);
		int difficultyDifferential = observedDifficulty - expectedDifficulty;
		
		int numberOfUserRatings = intValue(userEntity,"numRatings");
		int numberOfContentRatings = intValue(contentEntity,"numRatings");
		
		double reviewerTimeBias = reviewerTimeBias(numberOfContentRatings);
		double difficultyTimeBias = difficultyTimeBias(numberOfUserRatings);
		double strengthTimeBias = strengthTimeBias(numberOfUserRatings);
	
		int newUserStrength = (int) (userStrength - difficultyDifferential * strengthTimeBias);
		int newDifficultyRating = (int) (difficultyRating + difficultyDifferential * reviewerTimeBias * difficultyTimeBias);
		
		userEntity.setUnindexedProperty("userStrength", newUserStrength);
		contentEntity.setUnindexedProperty("difficultyRating", newDifficultyRating);
		
		// Updates the counts
		userEntity.setUnindexedProperty("numRatings", numberOfUserRatings + 1);
		contentEntity.setUnindexedProperty("numRatings", numberOfContentRatings + 1);
		
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
		double oldValue = (double) intValue(e,prop);
		int n = intValue(e, "numRatings");
		Double newValue = ((Double) (oldValue * n + rating * 10) / (n + 1));
		e.setUnindexedProperty(prop, newValue.intValue());
	}
	
	private static Entity getOrCreateContentRatingProfile(String contentUuid) {
		Key userKey = KeyFactory.createKey("ContentRatingProfile", contentUuid);
		try {
			return datastore.get(userKey);
		} catch (EntityNotFoundException e) {
			Entity result = new Entity(userKey);
			// NumRatings allows us to calculate averages, while not maintaining
			// references to individual ratings.
			result.setUnindexedProperty("numRatings", 0);
			
			// Average ones are use to gauge reactions against
			result.setUnindexedProperty("averageDifficulty", 500);
			result.setUnindexedProperty("averageQuality", 500);
			result.setUnindexedProperty("averageHelpfulness", 500);
			
			// Perceived strength tells us how difficult they like their questions
			result.setUnindexedProperty("difficultyRating", 500);
			
			// Quality aggregates karma in terms of hundredths:
			result.setUnindexedProperty("karma", 100);
			return result;
		}
	}
	
	private static boolean testAndSetContentRatedByUser(String contentUuid, String userId){
		Entity content = getOrCreateContentRatingProfile(contentUuid);
		List<String> ratedBy = SafeList.string(content, "ratedBy");
		boolean alreadyThere = false;
		if (ratedBy.contains(userId)){
			alreadyThere = true;
		}
		ratedBy.add(userId);
		content.setUnindexedProperty("ratedBy", ratedBy);
		datastore.put(content);
		return alreadyThere;
	}
	
	public static boolean contentRatedByUser(String contentUuid, String userId){
		Entity content = getOrCreateContentRatingProfile(contentUuid);
		List<String> ratedBy = SafeList.string(content, "ratedBy");
		if (ratedBy.contains(userId)) {
			return true;
		} else {
			return false;
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
			result.setUnindexedProperty("numRatings", 0);
			
			// Average ones are use to gauge reactions against
			result.setUnindexedProperty("averageDifficulty", 500);
			result.setUnindexedProperty("averageQuality", 500);
			result.setUnindexedProperty("averageHelpfulness", 500);
			
			// Perceived strength tells us how difficult they like their questions
			result.setUnindexedProperty("userStrength", 500);
			
			return result;
		}
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
	
	private static int intValue(Entity e, String p){
		Object o = e.getProperty(p);
		if (o instanceof Long){
			return ((Long) o).intValue();
		} else if (o instanceof Integer){
			return ((Integer) o).intValue();
		} else {
			return (int) o;
		}
	}
}
