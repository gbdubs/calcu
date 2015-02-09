package calculus.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculus.api.TagAPI;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InterestsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	public static Map<String, Boolean> getPotentialAndExistingInterests(String userId, int maxResults){
		Entity interestProfile = getInterestsEntity(userId);
		
		int offset = intValue(interestProfile, "popularTagOffset");
		interestProfile.setProperty("popularTagOffset", offset + maxResults);
		
		List<String> popularTags = TagAPI.getPopularTags(maxResults, offset);
		
		if (popularTags == null || popularTags.size() < maxResults || offset > 500){
			interestProfile.setProperty("popularTagOffset", 0);
		}
		
		Map<String, Boolean> seriousInterest = new HashMap<String, Boolean>();
		int seriousInterestThresholdValue = 6;
		
		for(String popularTag : popularTags){
			if (getInterestValue(interestProfile, popularTag) > seriousInterestThresholdValue){
				seriousInterest.put(popularTag, true);
			} else {
				seriousInterest.put(popularTag, false);
			}
		}

		datastore.put(interestProfile);

		return seriousInterest;
	}
	
	public static void incrementUserInterests(String userId, List<String> interestTags, float increment){
		Entity interestsEntity = getInterestsEntity(userId);
		for (String interestTag : interestTags){
			incrementUserInterest(interestsEntity, interestTag, increment);
			incrementTotalInterest(interestsEntity, interestTag, increment);
		}
		interestsEntity.setProperty("updatedAt", System.currentTimeMillis());
		datastore.put(interestsEntity);
	}

	protected static Entity getInterestsEntity(String userId){
		Key key = KeyFactory.createKey("InterestsProfile", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			Entity interestsEntity = new Entity(key);
			interestsEntity.setProperty("interestProgression", 0);
			datastore.put(interestsEntity);
			return interestsEntity;
		}
	}
	
	private static float getInterestValue(Entity interestsEntity, String tag){
		Float result = (Float) interestsEntity.getProperty(tag);
		if (result == null) return 0;
		return result;
	}


	private static void incrementTotalInterest(Entity interestsEntity, String interestTag, float increment) {
		Float interestProgression = (Float) interestsEntity.getProperty("interestProgression");
		if (interestProgression == null) interestProgression = new Float(0);
		float interestDifficulty = TagDifficultyAPI.getDifficulty(interestTag);
		if (interestDifficulty > interestProgression){
			interestProgression += increment * (interestDifficulty - interestProgression);
		}
		interestsEntity.setProperty("interestProgression", interestProgression);
	}

	private static void incrementUserInterest(Entity interestsEntity, String tag, float increment){
		Float interest = (Float) interestsEntity.getProperty(tag);
		if (interest == null) interest = new Float(0);
		float newInterest = interest + increment;
		interestsEntity.setUnindexedProperty(tag, newInterest);
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
