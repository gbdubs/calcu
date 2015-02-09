package calculus.recommendation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculus.api.BookmarksAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.TagAPI;
import calculus.api.HelpfulContentAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class MasterRecommendationsAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	// Havent' figured out how to integrate responsive programming in to this API, but I know it will be here somewhere...
	// private static final float randomProportion = (float) .10;
	// private static final float similarUserBookmarkProportion = (float) .20;
	// private static final float similarUserHighlyRatedProportion = (float) .20;
	// private static final float recommendationsIndexTagResults = (float) .50;
	
	private static final long updateInterval = 1000 * 60 * 60 * 24; // 1 Day
	
	public static List<String> getRecommendations(String userId){
		Key key = KeyFactory.createKey("Recommendations", userId);
		Entity e;
		try {
			e = datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			List<String> refreshedRecommendations = refreshRecommendations(userId);
			return refreshedRecommendations;
		}
		Long lastUpdated = (Long) e.getProperty("updatedAt");
		if (lastUpdated == null) lastUpdated = new Long(0);
		
		long sinceLastUpdate = System.currentTimeMillis() - lastUpdated;
		if (sinceLastUpdate > updateInterval){
			List<String> refreshedRecommendations = refreshRecommendations(userId);
			return refreshedRecommendations;
		} else {
			List<String> recs = (List<String>) e.getProperty("recommendations");
			if (recs == null) recs = new ArrayList<String>();
			return recs;
		}	
	}

	private static List<String> refreshRecommendations(String userId){
		Map<String, Integer> mapping = new HashMap<String, Integer>();
		
		List<String> similarUsers = UserGroupingAPI.getNSimilarUsers(userId, 20);
		for(String similarUser : similarUsers){
			List<String> bookmarks = BookmarksAPI.getUserBookmarks(similarUser);
			List<String> wellRated = HelpfulContentAPI.getHelpfulContent(similarUser);
			for (String b : bookmarks){
				increment(mapping, b);
			}
			for (String wr : wellRated){
				increment(mapping, wr);
			}
		}
		
		List<String> tags = RecommendationIndexAPI.getRecommendedTags(userId, 20);
		List<String> contentFromTags = TagAPI.getUuidsResultsOfMultipleTags(tags, 50, 0);
		for (String cft : contentFromTags){
			increment(mapping, cft);
		}
		
		List<String> refreshedRecommendations = getDecendingListFromCountMap(mapping);
		
		Key key = KeyFactory.createKey("Recommendations", userId);
		Entity e = new Entity(key);
		e.setUnindexedProperty("recommendations", refreshedRecommendations);
		e.setProperty("updatedAt", System.currentTimeMillis());
		datastore.put(e);
		
		return refreshedRecommendations;
	}
	
	public static void increment(Map<String, Integer> map, String s) {
		if (map.containsKey(s)) {
			map.put(s, map.get(s) + 1);
		} else {
			map.put(s, 1);
		}
	}


	
	
	public static PracticeProblem getDifficultyCalibrationPracticeProblem(String userId) {
		return PracticeProblemAPI.getAnswerablePracticeProblem(userId);
	}
	
	public static Question getDifficultyCalibrationQuestion(String userId) {
		return QuestionAPI.getAnswerableQuestion(userId);
	}

	public static void andComparisonInformation(String userId, String preference, String[] allUuids) {
		System.out.println("User ["+userId+"] chose content ["+preference+"] out of the possibilities ["+Arrays.toString(allUuids)+"].");
		
	}
	
	private static List<String> getDecendingListFromCountMap(Map<String, Integer> mapping){
		List<String> result = new ArrayList<String>();
		for(String str : mapping.keySet()){
			int i = 0;
			Integer value = mapping.get(str);
			while (i < result.size() && mapping.get(result.get(i)) <= value){
				i++;
			}
			result.add(i, str);
		}
		return result;
	}
}
