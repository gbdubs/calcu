package calculus.recommendation;

import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class RecommendationIndexAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	public static void updateUserRecommendations(String userId){
		Entity recommendationsEntity = getRecommendationsEntity(userId);
		Entity interestsEntity = InterestsAPI.getInterestsEntity(userId);
		Entity skillsEntity = SkillsAPI.getSkillsEntity(userId);
		
		Map<String, Object> interests = interestsEntity.getProperties();
		Map<String, Object> skills = skillsEntity.getProperties();
		
		for (String tag : interests.keySet()){
			float interest = floatValue(interests, tag);
			float skill = floatValue(skills, tag);
			
			float recommendationIndex = interest - skill;

			recommendationsEntity.setUnindexedProperty(tag, recommendationIndex);
		}
		
		recommendationsEntity.setProperty("updatedAt", System.currentTimeMillis());
		datastore.put(recommendationsEntity);
	}

	private static float floatValue(Map<String, Object> interests, String tag) {
		Object o = interests.get(tag);
		if (o instanceof Float) {
			return (Float) o;
		} else if (o == null) {
			return 0;
		} else {
			return (Float) o;
		}
	}

	private static Entity getRecommendationsEntity(String userId){
		Key key = KeyFactory.createKey("RecommendationsProfile", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			Entity recommendationsEntity = new Entity(key);
			datastore.put(recommendationsEntity);
			updateUserRecommendations(userId);
			return recommendationsEntity;
		}
	}
}
