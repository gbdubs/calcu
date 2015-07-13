package calculus.recommendation;

import java.util.List;

import calculus.api.ContentAPI;
import calculus.models.Content;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class SkillsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	private static final float answerQuestionIncrement = 1;
	private static final float difficultyPersonalizationCalibrationFactor = (float) -.5;
	private static final float difficultyRatingCalibrationFactor = (float) -.5;
	
	public static void userAnsweredContent(String userId, String contentUuid){
		Content c;
		try {
			c = ContentAPI.instantiateContent(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't mark that the user answered it.
			return;
		}
		List<String> contentTags = c.getListOfTags();

		incrementUserSkills(userId, contentTags, answerQuestionIncrement);
	}
	
	/**
	 * Calibrates to the difficulty of the person.
	 * @param userId The User's ID
	 * @param contentUuid Content that was personalzied to
	 * @param difficulty A Float that should be between 0 and 1
	 */
	public static void contentDifficultyPersonalization(String userId, String contentUuid, float difficulty){
		Content c;
		try {
			c = ContentAPI.instantiateContent(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the skills profile
			return;
		}
		List<String> contentTags = c.getListOfTags();

		incrementUserSkills(userId, contentTags, difficulty * difficultyPersonalizationCalibrationFactor);
	}
	
	public static void userRatedContentThisDifficulty(String userId, String contentUuid, float difficultyRating){
		Content c;
		try {
			c = ContentAPI.instantiateContent(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the skills profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserSkills(userId, contentTags, difficultyRating * difficultyRatingCalibrationFactor);
	}
	
	private static void incrementUserSkills(String userId, List<String> skillTags, float increment){
		Entity skillsEntity = getSkillsEntity(userId);
		for (String skillTag : skillTags){
			incrementUserSkill(skillsEntity, skillTag, increment);
			incrementTotalSkill(skillsEntity, skillTag, increment);
		}
		skillsEntity.setUnindexedProperty("updatedAt", System.currentTimeMillis());
		datastore.put(skillsEntity);
	}

	protected static Entity getSkillsEntity(String userId){
		Key key = KeyFactory.createKey("SkillsProfile", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			Entity skillsEntity = new Entity(key);
			skillsEntity.setUnindexedProperty("skillProgression", 0);
			datastore.put(skillsEntity);
			return skillsEntity;
		}
	}
	
	private static void incrementTotalSkill(Entity skillsEntity, String skillTag, float increment) {
		Float skillProgression = floatValue(skillsEntity, "skillProgression");
		if (skillProgression == null) skillProgression = new Float(0);
		float skillDifficulty = TagDifficultyAPI.getDifficulty(skillTag);
		if (skillDifficulty > skillProgression){
			skillProgression += increment * (skillDifficulty - skillProgression);
		}
		skillsEntity.setUnindexedProperty("skillProgression", skillProgression);
	}

	private static void incrementUserSkill(Entity skillsEntity, String tag, float increment){
		Float skill = floatValue(skillsEntity, tag);
		if (skill == null) skill = new Float(0);
		float newSkill = skill + increment;
		skillsEntity.setUnindexedProperty(tag, newSkill);
	}
	
	private static Float floatValue(Entity e, String p){
		Object o = e.getProperty(p);
		if (o instanceof Double){
			return ((Double) o).floatValue();
		} else if (o instanceof Float){
			return (Float) o;
		} else if (o instanceof Integer){
			return ((Integer) o).floatValue();
		} else {
			return null;
		}
	}
	
}
