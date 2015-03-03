package calculus.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculus.api.TagAPI;
import calculus.models.Content;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InterestsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	private static final float userViewedContentIncrement = (float) .2;
	private static final float userRatedContentIncrement = (float) .1;
	private static final float userAnsweredContentIncrement = (float) .1;
	private static final float userTagCalibrationIncrement = (float) 1;
	private static final float userSearchedForTagIncrement = (float) .5;
	private static final float userFoundContentDisinteresting = (float) -.2;
	private static final float userFoundContentInteresting = (float) .2;
	private static final float userBookmarkedContentIncrement = (float) .3;
	private static final int seriousInterestThresholdValue = 6;
	
	public static void userViewedContent(String userId, String contentUuid){
		Content c;
		try {
			c = new Content(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the interests profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserInterests(userId, contentTags, userViewedContentIncrement);
	}
	
	public static void userRatedContent(String userId, String contentUuid){
		Content c;
		try {
			c = new Content(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the interests profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserInterests(userId, contentTags, userRatedContentIncrement);
	}
	
	public static void userFoundContentDisinteresting(String userId, String contentUuid){
		Content c;
		try {
			c = new Content(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the interests profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserInterests(userId, contentTags, userFoundContentDisinteresting);
	}
	
	public static void userAnsweredContent(String userId, String contentUuid){
		Content c;
		try {
			c = new Content(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the interests profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserInterests(userId, contentTags, userAnsweredContentIncrement);
	}
	
	public static void userBookmarkedContent(String userId, String contentUuid){
		Content c;
		try {
			c = new Content(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the interests profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserInterests(userId, contentTags, userBookmarkedContentIncrement);
	}
	
	public static void userIndicatedTagsInterestingInCalibration(String userId, List<String> tags){
		incrementUserInterests(userId, tags, userTagCalibrationIncrement);
	}
	
	public static void userIndicatedTagsDisinterestingInCalibration(String userId, List<String> tags){
		incrementUserInterests(userId, tags, -1 * userTagCalibrationIncrement);
	}
	
	public static void userSearchedForTags(String userId, List<String> tags){
		incrementUserInterests(userId, tags, userSearchedForTagIncrement);
	}
	
	public static Map<String, Boolean> getPotentialAndExistingInterests(String userId, int maxResults){
		Entity interestProfile = getInterestsEntity(userId);
		
		int offset = intValue(interestProfile, "popularTagOffset");
		interestProfile.setUnindexedProperty("popularTagOffset", offset + maxResults);
		
		List<String> popularTags = TagAPI.getPopularTags(maxResults, offset);
		
		if (popularTags == null || popularTags.size() < maxResults || offset > 500){
			interestProfile.setUnindexedProperty("popularTagOffset", 0);
		}
		
		Map<String, Boolean> seriousInterest = new HashMap<String, Boolean>();
		
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
	
	// COST: 1 READ, 4 WRITE
	private static void incrementUserInterests(String userId, List<String> interestTags, float increment){
		Entity interestsEntity = getInterestsEntity(userId);
		for (String interestTag : interestTags){
			incrementUserInterest(interestsEntity, interestTag, increment);
			incrementTotalInterest(interestsEntity, interestTag, increment);
		}
		interestsEntity.setUnindexedProperty("updatedAt", System.currentTimeMillis());
		datastore.put(interestsEntity);
	}

	protected static Entity getInterestsEntity(String userId){
		Key key = KeyFactory.createKey("InterestsProfile", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			Entity interestsEntity = new Entity(key);
			interestsEntity.setUnindexedProperty("interestProgression", 0);
			return interestsEntity;
		}
	}
	
	private static float getInterestValue(Entity interestsEntity, String tag){
		Float result = floatValue(interestsEntity, tag);
		if (result == null) return 0;
		return result;
	}


	private static void incrementTotalInterest(Entity interestsEntity, String interestTag, float increment) {
		int interestProgression = intValue(interestsEntity,"interestProgression");
		float interestDifficulty = (float) TagDifficultyAPI.getDifficulty(interestTag);
		if (interestDifficulty > interestProgression){
			interestProgression += increment * (interestDifficulty - interestProgression);
		}
		interestsEntity.setUnindexedProperty("interestProgression", interestProgression);
	}

	private static void incrementUserInterest(Entity interestsEntity, String tag, float increment){
		Float interest = floatValue(interestsEntity, tag);
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
		} else if (o == null){
			return 0;
		} else {
			return (Integer) o;
		}
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

	public static void userFoundContentInteresting(String userId, String contentUuid) {
		Content c;
		try {
			c = new Content(contentUuid);
		} catch (EntityNotFoundException e) {
			// If the entity didn't exist, we shouldn't change the interests profile
			return;
		}
		List<String> contentTags = c.getListOfTags();
		
		incrementUserInterests(userId, contentTags, userFoundContentInteresting);
	}
}
