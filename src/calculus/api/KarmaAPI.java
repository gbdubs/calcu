package calculus.api;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Content;
import calculus.models.Notification;
import calculus.utilities.Settings;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class KarmaAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static int getLevel(int karma){
		int level = 0;
		while (karma >= 0){
			level = level + 1;
			karma -= level * 10;
		}
		return level;
	}
	
	public static int getKarmaThresholdForLevel(int level){
		int karma = 5 * (level * (level - 1));
		return karma;
	}
	
	public static int getLevel(String userId){
		return getLevel(calculateTotalKarma(userId));
	}
	
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
			int oldValue = intValue(karmaProfile, propertyToIncrement);
			karmaProfile.setUnindexedProperty(propertyToIncrement, oldValue + differential);
			datastore.put(karmaProfile);
		} catch (EntityNotFoundException e) {
			// Don't worry aobut it, and dont' increment any karma!
		}
	}
	
	public static void incrementUserKarmaForRatingOthers(String raterId, int differential){
		// Increments the "KarmaProfile" entity
		Entity karmaProfile = getUserKarmaProfile(raterId);
		karmaProfile.setUnindexedProperty("karmaFromRatingOthers", intValue(karmaProfile,"karmaFromRatingOthers") + differential);
		datastore.put(karmaProfile);
		
		// Updates the "UserPublicInfo" entity
		incrementUserKarma(raterId, differential);
	}

	public static void incrementUserKarmaFromAnswerMode(String answererId, int differential){
		// Increments the "KarmaProfile" entity
		Entity karmaProfile = getUserKarmaProfile(answererId);
		karmaProfile.setUnindexedProperty("karmaFromAnswerMode", intValue(karmaProfile, "karmaFromAnswerMode") + differential);
		datastore.put(karmaProfile);
		
		// Updates the "UserPublicInfo" entity
		incrementUserKarma(answererId, differential);
	}
	
	public static void incrementUserKarmaFromApprovedAnswers(String answererId, int differential){
		// Increments the "KarmaProfile" entity
		Entity karmaProfile = getUserKarmaProfile(answererId);
		karmaProfile.setUnindexedProperty("karmaFromApprovedAnswers", intValue(karmaProfile, "karmaFromApprovedAnswers") + differential);
		datastore.put(karmaProfile);
		
		// Updates the "UserPublicInfo" entity
		incrementUserKarma(answererId, differential);
	}
	
	private static void incrementUserKarma(String userId, int differential) {
		Entity userPublicInfo = UserPublicInfoAPI.getOrCreateUserPublicInfo(userId);
		
		// Updates Karma
		int karma = ((Long) userPublicInfo.getProperty("karma")).intValue();
		userPublicInfo.setProperty("karma", karma + differential);
		
		// Updates Level
		int intialLevel = ((Long) userPublicInfo.getProperty("level")).intValue();
		int newLevel = KarmaAPI.getLevel(karma + differential);
		if (intialLevel != newLevel){
			userPublicInfo.setUnindexedProperty("level", newLevel);
			Notification levelUp = levelUpNotification(userId, newLevel);
			NotificationsAPI.sendNotification(levelUp);
		}
		
		// Stores Changes
		datastore.put(userPublicInfo);
	}

	private static Notification levelUpNotification(String userId, int newLevel) {
		String title = "You Reached Level " + newLevel + "!";
		String body = "Because you reached " + KarmaAPI.getKarmaThresholdForLevel(newLevel) + " Karma, you are now Level " + newLevel + "! Click to see your Karma Profile!";
		String url = "/user/" + userId + "#karma";
		
		Notification n = new Notification()
			.withRecipientId(userId)
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withTimeNow()
			.withTitle(title)
			.withBody(body)
			.withUrl(url)
			.withColor(ContentAPI.randomColor());
	
		return n;
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
	
	private static int calculateTotalKarma (String userId){
		Entity karmaProfile = getUserKarmaProfile(userId);
		int pp = intValue(karmaProfile, "karmaFromPracticeProblems");
		int q = intValue(karmaProfile, "karmaFromQuestions");
		int tc = intValue(karmaProfile, "karmaFromTextContent");
		int a = intValue(karmaProfile, "karmaFromAnswers");
		int r = intValue(karmaProfile, "karmaFromRatingOthers");
		int am = intValue(karmaProfile, "karmaFromAnswerMode");
		int aa = intValue(karmaProfile, "karmaFromApprovedAnswers");
		return pp + q + tc + a + r + am + aa;
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

	public static void setKarmaProfileAttributes(HttpServletRequest req, String userId){
		Entity karmaProfile = KarmaAPI.getUserKarmaProfile(userId);
		req.setAttribute("karmaFromPracticeProblems", karmaProfile.getProperty("karmaFromPracticeProblems"));
		req.setAttribute("karmaFromQuestions", karmaProfile.getProperty("karmaFromQuestions"));
		req.setAttribute("karmaFromTextContent", karmaProfile.getProperty("karmaFromTextContent"));
		req.setAttribute("karmaFromAnswers", karmaProfile.getProperty("karmaFromAnswers"));
		req.setAttribute("karmaFromRatingOthers", karmaProfile.getProperty("karmaFromRatingOthers"));
		req.setAttribute("karmaFromAnswerMode", karmaProfile.getProperty("karmaFromAnswerMode"));
		req.setAttribute("karmaFromApprovedAnswers", karmaProfile.getProperty("karmaFromApprovedAnswers"));
		req.setAttribute("karmaTotal", calculateTotalKarma(userId));
	}

}