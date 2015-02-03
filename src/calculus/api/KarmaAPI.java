package calculus.api;

import calculus.models.Content;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class KarmaAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static void incrementContentKarma(String contentUuid, int differential){
		try {
			Content c = new Content(contentUuid);
			
			// Updates the "Content" entity
			c.incrementKarma(differential);
			String contentType = c.getContentType();
			String authorId = c.getAuthor().getUserId();
			
			// Updates the "UserPublicInfo" entity for the AUTHOR
			incrementUserKarma(authorId, differential);
			
			// Updates the "KarmaProfile" entity
			Entity karmaProfile = getUserKarmaProfile(authorId);
			String propertyToIncrement = "";
			if (contentType.equals("question")){
				propertyToIncrement = "karmaFromQuestions";
			} else if (contentType.equals("practiceProblem")){
				propertyToIncrement = "karmaFromPracticeProblems";
			} else if (contentType.equals("textContent")){
				propertyToIncrement = "karmaFromTextContent";
			} else if (contentType.equals("answer")){
				propertyToIncrement = "karmaFromAnswers";
			} else {
				return;
			}
			int oldValue = (int) karmaProfile.getProperty(propertyToIncrement);
			karmaProfile.setProperty(propertyToIncrement, oldValue + differential);
			datastore.put(karmaProfile);
		} catch (EntityNotFoundException e) {
			// Don't worry aobut it, and dont' increment any karma!
		}
	}
	
	public static void incrementUserKarmaForRatingOthers(String raterId, int differential){
		// Increments the "KarmaProfile" entity
		Entity karmaProfile = getUserKarmaProfile(raterId);
		karmaProfile.setUnindexedProperty("karmaFromRatingOthers", (int) karmaProfile.getProperty("karmaFromRatingOthers") + differential);
		datastore.put(karmaProfile);
		
		// Updates the "UserPublicInfo" entity
		incrementUserKarma(raterId, differential);
	}

	public static void incrementUserKarmaFromAnswerMode(String answererId, int differential){
		// Increments the "KarmaProfile" entity
		Entity karmaProfile = getUserKarmaProfile(answererId);
		karmaProfile.setUnindexedProperty("karmaFromAnswerMode", (int) karmaProfile.getProperty("karmaFromAnswerMode") + differential);
		datastore.put(karmaProfile);
		
		// Updates the "UserPublicInfo" entity
		incrementUserKarma(answererId, differential);
	}
	
	public static void incrementUserKarmaFromApprovedAnswers(String answererId, int differential){
		// Increments the "KarmaProfile" entity
		Entity karmaProfile = getUserKarmaProfile(answererId);
		karmaProfile.setUnindexedProperty("karmaFromApprovedAnswers", (int) karmaProfile.getProperty("karmaFromApprovedAnswers") + differential);
		datastore.put(karmaProfile);
		
		// Updates the "UserPublicInfo" entity
		incrementUserKarma(answererId, differential);
	}
	
	private static void incrementUserKarma(String userId, int differential) {
		Entity userPublicInfo = UserPublicInfoAPI.getOrCreateUserPublicInfo(userId);
		int karma = ((Long) userPublicInfo.getProperty("karma")).intValue();
		userPublicInfo.setProperty("karma", karma + differential);
		datastore.put(userPublicInfo);
	}

	private static Entity getUserKarmaProfile(String userId){
		Key key = KeyFactory.createKey("UserKarmaProfile", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			Entity result = new Entity(key);
			result.setUnindexedProperty("karmaFromPracticeProblems", 0);
			result.setUnindexedProperty("karmaFromQuestions", 0);
			result.setUnindexedProperty("karmaFromTextContent", 0);
			result.setUnindexedProperty("karmaFromAnswers", 0);
			result.setUnindexedProperty("karmaFromRatingOthers", 0);
			result.setUnindexedProperty("karmaFromAnswerMode", 0);
			result.setUnindexedProperty("karmaFromApprovedAnswers", 0);
			datastore.put(result);
			return result;
		}
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