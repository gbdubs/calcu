package calculus.recommendation;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class SkillsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	public static void incrementUserSkills(String userId, List<String> skillTags, float increment){
		Entity skillsEntity = getSkillsEntity(userId);
		for (String skillTag : skillTags){
			incrementUserSkill(skillsEntity, skillTag, increment);
			incrementTotalSkill(skillsEntity, skillTag, increment);
		}
		datastore.put(skillsEntity);
	}

	protected static Entity getSkillsEntity(String userId){
		Key key = KeyFactory.createKey("SkillsProfile", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			Entity skillsEntity = new Entity(key);
			skillsEntity.setProperty("skillProgression", 0);
			datastore.put(skillsEntity);
			return skillsEntity;
		}
	}
	
	private static void incrementTotalSkill(Entity skillsEntity, String skillTag, float increment) {
		Float skillProgression = (Float) skillsEntity.getProperty("skillProgression");
		if (skillProgression == null) skillProgression = new Float(0);
		float skillDifficulty = TagProgression.getDifficulty(skillTag);
		if (skillDifficulty > skillProgression){
			skillProgression += increment * (skillDifficulty - skillProgression);
		}
		skillsEntity.setProperty("skillProgression", skillProgression);
	}

	private static void incrementUserSkill(Entity skillsEntity, String tag, float increment){
		Float skill = (Float) skillsEntity.getProperty(tag);
		if (skill == null) skill = new Float(0);
		float newSkill = skill + increment;
		skillsEntity.setUnindexedProperty(tag, newSkill);
	}
	
}
