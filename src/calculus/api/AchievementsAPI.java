package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import calculus.models.Achievement;
import calculus.models.Notification;
import calculus.utilities.SafeList;
import calculus.utilities.Settings;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class AchievementsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static String createNewAchievement(String name, String icon, String color, String secondaryColor, String description, String qualification){
		String AchievementId = UUID.randomUUID().toString();
		
		Key key = KeyFactory.createKey("Achievement", AchievementId);
		
		Entity entity = new Entity(key);
		entity.setUnindexedProperty("name", name);
		entity.setUnindexedProperty("icon", icon);
		entity.setUnindexedProperty("color", color);
		entity.setUnindexedProperty("secondaryColor", secondaryColor);
		entity.setUnindexedProperty("qualification", qualification);
		entity.setUnindexedProperty("description", description);
		entity.setUnindexedProperty("uuid", AchievementId);
		
		datastore.put(entity);
		
		return AchievementId;
	}
	
	public static String createNewAchievementWithUuid(String uuid, String name, String icon, String textColor, String backgroundColor, String description, String qualification) {
		Key key = KeyFactory.createKey("Achievement", uuid);
		
		Entity entity = new Entity(key);
		entity.setUnindexedProperty("name", name);
		entity.setUnindexedProperty("icon", icon);
		entity.setUnindexedProperty("color", textColor);
		entity.setUnindexedProperty("secondaryColor", backgroundColor);
		entity.setUnindexedProperty("qualification", qualification);
		entity.setUnindexedProperty("description", description);
		entity.setUnindexedProperty("uuid", uuid);
		
		datastore.put(entity);
		
		return uuid;
	}

	public static void deleteAchievement(String uuid) {
		if (uuid != null)
			datastore.delete(KeyFactory.createKey("Achievement", uuid));
	}

	public static void deleteAllAchievements() {
		Query query = new Query("Achievement").setKeysOnly();
		PreparedQuery pq = datastore.prepare(query);
		for(Entity entity : pq.asIterable()){
			datastore.delete(entity.getKey());
		}
	}

	private static List<String> getAllAchievementUuids() {
		List<String> result = new ArrayList<String>();
		Query query = new Query("Achievement");
		PreparedQuery pq = datastore.prepare(query);
		for(Entity entity : pq.asIterable()){
			result.add((String) entity.getProperty("uuid"));
		}
		return result;
	}

	private static List<String> getUserAchievementUuids(String userId){
		Entity e = getUserAchievementEntity(userId);
		return SafeList.string(e, "achievements");
	}

	private static List<String> getUnfinishedAchievementUuids(String userId){
		List<String> allAchievements = getAllAchievementUuids();
		List<String> userAchievements = getUserAchievementUuids(userId);
		allAchievements.removeAll(userAchievements);
		return allAchievements;
	}

	public static List<Achievement> getAchievements(List<String> uuids){
		List<Achievement> Achievements = new ArrayList<Achievement>();
		for (String uuid : uuids){
			try{
				Achievements.add(new Achievement(uuid));
			} catch (RuntimeException e){
				// Ignore this, it is likely a datastore update issue.
			}
		}
		return Achievements;
	}

	public static List<Achievement> getAllAchievements(){
		return getAchievements(getAllAchievementUuids());
	}

	public static List<Achievement> getUserAchievements(String userId){
		return getAchievements(getUserAchievementUuids(userId));
	}
	
	public static List<Achievement> getUserUnfinishedAchievements(String userId){
		return getAchievements(getUnfinishedAchievementUuids(userId));
	}
	
	private static Notification createAchievementAlert(String userId, String achievementUuid) {
		Achievement a = new Achievement(achievementUuid);
		String title = "New Achievement!";
		String body = "You just completed the " +a.getName() + " achievement! Check them out here!";
		String url = "/achievements";
		
		String imageUrl = "/_static/img/achievement-badges/" + a.getName() + ".png";
		
		Notification n = new Notification()
			.withRecipientId(userId)
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withImageUrl(imageUrl)
			.withTimeNow()
			.withTitle(title)
			.withBody(body)
			.withUrl(url)
			.withColor("warning");
		
		return n;
	}
	
	private static Entity getUserAchievementEntity(String userId){
		Key key = KeyFactory.createKey("UserAchievementProfile", userId);
		try {
			Entity result = datastore.get(key);
			return result;
		} catch (EntityNotFoundException e) {
			Entity result = new Entity(key);
			result.setUnindexedProperty("htmlContent", new Long(0));
			result.setUnindexedProperty("latexContent", new Long(0));
			result.setUnindexedProperty("allContent", new Long(0));
			result.setUnindexedProperty("allPracticeProblems", new Long(0));
			result.setUnindexedProperty("allQuestions", new Long(0));
			result.setUnindexedProperty("allTextContent", new Long(0));
			result.setUnindexedProperty("allAnswers", new Long(0));
			result.setUnindexedProperty("personalized", new Long(0));
			result.setUnindexedProperty("bestAnswerModeStreak", new Long(0));
			setUserAchievement(result, "0465ae81-256c-48e6-ba57-f6b7af62b3f1");
			datastore.put(result);
			return result;
		}
	}
	
	public static void incrementUserStats(String userId, String[] properties){
		List<String> toPass = new ArrayList<String>();
		for (String p : properties) toPass.add(p);
		incrementUserStats(userId, toPass);
	}
	
	public static void incrementUserStats(String userId, List<String> properties){
		Entity achievementEntity = getUserAchievementEntity(userId);
		boolean changed = false;
		for (String prop : properties){
			Long result = (Long) achievementEntity.getProperty(prop);
			if (result != null){
				result++;
				changed = true;
				achievementEntity.setUnindexedProperty(prop, result);
			}
		}
		if (changed){
			checkForNewAchievements(achievementEntity);
			datastore.put(achievementEntity);
		}
	}

	public static boolean hasUserPersonalized(String userId){
		Entity e = getUserAchievementEntity(userId);
		Long l = (Long) e.getProperty("personalized");
		if (l == null) return false;
		if (l <= 0) return false;
		return true;
	}

	private static void setUserAchievement(Entity e, String achievementUuid){
		List<String> achievements = SafeList.string(e, "achievements");
		if (! achievements.contains(achievementUuid)){
			achievements.add(achievementUuid);
			e.setUnindexedProperty("achievements", achievements);
			Notification n = createAchievementAlert(e.getKey().getName(), achievementUuid);
			NotificationsAPI.sendNotification(n);
		}
	}
	
	private static void checkForNewAchievements(Entity e) {
		
		Long allContent = (Long) e.getProperty("allContent");
		Long latexContent = (Long) e.getProperty("latexContent");
		Long htmlContent = (Long) e.getProperty("htmlContent");
		Long personalized = (Long) e.getProperty("personalized");
		Long allAnswers = (Long) e.getProperty("allAnswers");
		Long answerMode = (Long) e.getProperty("bestAnswerModeStreak");
		
		if (allContent == 5){
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f4");
		} else if (allContent == 9) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b316");
		} else if (allContent == 10) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f5");
		} else if (allContent == 20) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f6");
		} else if (allContent == 50) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f7");
		} else if (allContent == 100){
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b310");
		}
		
		if (htmlContent == 3) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f2");
		}
		
		if (latexContent == 3) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f3");
		}
		
		if (allAnswers == 10) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f8");
		} else if (allAnswers == 20) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b3f9");
		}
		
		if (personalized == 3) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b312");
		} else if (personalized == 7) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b313");
		} else if (personalized == 5) {
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b320");
		}
		
		if (answerMode == 42){
			setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b317");
		}	
	}

	public static void brandeisanAchievement(String userId) {
		Entity e = getUserAchievementEntity(userId);
		setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b315");
		datastore.put(e);
	}

	public static void reporterAchievement(String userId) {
		Entity e = getUserAchievementEntity(userId);
		setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b311");
		datastore.put(e);
	}
	
	public static void incrementUserAchievementStatsFromContentSubmission(String userId, String body, String contentType){
		List<String> toIncrement = new ArrayList<String>();
		toIncrement.add("allContent");
		toIncrement.add("all"+contentType);
		if (body.contains("/(") || body.contains("$$")){
			toIncrement.add("latexContent");
		}
		if (body.contains("<p>")||body.contains("<li>")||body.contains("<b>")||body.contains("<br/>")){
			toIncrement.add("htmlContent");
		}
		incrementUserStats(userId, toIncrement);
	}

	public static void incrementUserAchievementStatsFromAnswerMode(String userId, int currentStreak, String body) {
		List<String> toIncrement = new ArrayList<String>();
		toIncrement.add("allContent");
		toIncrement.add("allAnswers");
		
		if (body.contains("\\(") || body.contains("$$")){
			toIncrement.add("latexContent");
		}
		if (body.contains("<p>")||body.contains("<li>")||body.contains("<b>")||body.contains("<br/>")){
			toIncrement.add("htmlContent");
		}
		
		Entity achievementEntity = getUserAchievementEntity(userId);
		boolean changed = false;
		Long bestAnswerModeStreak = (Long) achievementEntity.getProperty("bestAnswerModeStreak");
		if (bestAnswerModeStreak < currentStreak){
			achievementEntity.setProperty("bestAnswerModeStreak", new Long(currentStreak));
			changed = true;
		}
		for (String prop : toIncrement){
			Long result = (Long) achievementEntity.getProperty(prop);
			if (result != null){
				result++;
				changed = true;
				achievementEntity.setUnindexedProperty(prop, result);
			}
		}
		if (changed){
			checkForNewAchievements(achievementEntity);
			datastore.put(achievementEntity);
		}
	}

	public static void loveAchievement(String authorId) {
		Entity e = getUserAchievementEntity(authorId);
		setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b318");
		datastore.put(e);
	}

	public static void levelUpAchievment(String userId) {
		Entity e = getUserAchievementEntity(userId);
		setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b319");
		datastore.put(e);
	}

	public static void brevityAchievement(String userId) {
		Entity e = getUserAchievementEntity(userId);
		setUserAchievement(e, "0465ae81-256c-48e6-ba57-f6b7af62b314");
		datastore.put(e);
	}
}
