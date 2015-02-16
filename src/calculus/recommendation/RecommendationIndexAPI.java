package calculus.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class RecommendationIndexAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	private static final String recommendationsTagsKey = "l9fm20vjm3ufd9m3jd09jsidmwh9emdiwk2j3hjd";
	
	
	protected static List<String> getRecommendedTags(String userId, int n){
		Entity recommendationsEntity = getRecommendationsEntity(userId);
		List<String> tags = getRecommendations(recommendationsEntity);
		if (tags.size() < n){
			return tags;
		}
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < n; i++){
			result.add(tags.get(i));
		}
		return result;
	}

	protected static void updateUserRecommendations(String userId){
		Entity recommendationsEntity = getRecommendationsEntity(userId);
		Entity interestsEntity = InterestsAPI.getInterestsEntity(userId);
		Entity skillsEntity = SkillsAPI.getSkillsEntity(userId);
		
		Long recommendationsUpdatedAt = (Long) recommendationsEntity.getProperty("updatedAt");
		if (recommendationsUpdatedAt == null) recommendationsUpdatedAt = new Long(0);
		Long interestsUpdatedAt = (Long) interestsEntity.getProperty("updatedAt");
		if (interestsUpdatedAt == null) interestsUpdatedAt = new Long(0);
		Long skillsUpdatedAt = (Long) skillsEntity.getProperty("updatedAt");
		if (skillsUpdatedAt == null) skillsUpdatedAt = new Long(0);
		
		if (interestsUpdatedAt <= recommendationsUpdatedAt && skillsUpdatedAt <= recommendationsUpdatedAt){
			// If the recommendations are older than the skills and the interests, we don't need to update.
			return;
		}
		
		Map<String, Object> interests = interestsEntity.getProperties();
		Map<String, Object> skills = skillsEntity.getProperties();
		
		Map<String, Float> allRecommendationIndicies = new HashMap<String, Float>();
		
		for (String tag : interests.keySet()){
			float interest = floatValue(interests, tag);
			float skill = floatValue(skills, tag);
			
			float recommendationIndex = interest - skill;
			allRecommendationIndicies.put(tag, recommendationIndex);
			
			if (!tag.equals(recommendationsTagsKey)){
				recommendationsEntity.setUnindexedProperty(tag, recommendationIndex);
			}
		}
		
		List<String> topRecommendedTags = getDecendingListFromCountMap(allRecommendationIndicies);
		
		setRecommendations(recommendationsEntity, topRecommendedTags);
		
		recommendationsEntity.setUnindexedProperty("updatedAt", System.currentTimeMillis());
		datastore.put(recommendationsEntity);
	}
	
	private static float floatValue(Map<String, Object> interests, String tag) {
		Object o = interests.get(tag);
		if (o instanceof Float) {
			return (Float) o;
		} else if (o == null) {
			return 0;
		} else if (o instanceof Double){
			return ((Double) o).floatValue();
		} else if (o instanceof Long){
			return ((Long) o).floatValue();
		} else {
			return (Float) o;
		}
	}

	private static List<String> getRecommendations(Entity entity){
		List<String> result = (List<String>) entity.getProperty(recommendationsTagsKey);
		if (result == null) result = new ArrayList<String>();
		return result;
	}
	
	private static void setRecommendations(Entity entity, List<String> recommendations){
		entity.setUnindexedProperty(recommendationsTagsKey, recommendations);
		datastore.put(entity);
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
	
	private static List<String> getDecendingListFromCountMap(Map<String, Float> mapping){
		List<String> result = new ArrayList<String>();
		for(String str : mapping.keySet()){
			int i = 0;
			float value = mapping.get(str);
			while (i < result.size() && mapping.get(result.get(i)) <= value){
				i++;
			}
			result.add(i, str);
		}
		return result;
	}
}
