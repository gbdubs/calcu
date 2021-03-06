package calculus.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculus.api.BookmarksAPI;
import calculus.api.TagAPI;
import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class MasterRecommendationsAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	
	// Havent' figured out how to integrate responsive programming in to this API, but I know it will be here somewhere...
	// private static final float randomProportion = (float) .10;
	// private static final float similarUserBookmarkProportion = (float) .20;
	// private static final float similarUserHighlyRatedProportion = (float) .20;
	// private static final float recommendationsIndexTagResults = (float) .50;
	
	private static final long updateInterval = 1000 * 60 * 60 * 24; // 1 Day
	
	protected static List<String> getRecommendations(String userId){
		Entity e = getRecommendationsEntity(userId);
		List<String> recs = SafeList.string(e, "recommendations");
		List<String> hidden = SafeList.string(e, "hiddenRecommendations");
		recs.removeAll(hidden);
		return recs;
	}
	
	protected static void hideRecommendation(String userId, String contentUuid){
		Entity e = getRecommendationsEntity(userId);
		List<String> hidden = SafeList.string(e, "hiddenRecommendations");
		hidden.add(contentUuid);
		e.setUnindexedProperty("hiddenRecommendations", hidden);
		asyncDatastore.put(e);
	}

	protected static void markDisinterestedRecommendation(String userId, String contentUuid) {
		Entity e = getRecommendationsEntity(userId);
		List<String> disinterested = SafeList.string(e, "disinterestedRecommendations");
		if (!disinterested.contains(contentUuid)){
			List<String> interested = SafeList.string(e, "interestedRecommendations");
			interested.remove(contentUuid);
			disinterested.add(contentUuid);
			e.setUnindexedProperty("disinterestedRecommendations", disinterested);
			e.setUnindexedProperty("interestedRecommendations", interested);
			asyncDatastore.put(e);
		}
	}
	
	protected static void markInterestedRecommendation(String userId, String contentUuid) {
		Entity e = getRecommendationsEntity(userId);
		List<String> interested = SafeList.string(e, "interestedRecommendations");
		if (!interested.contains(contentUuid)){
			List<String> disinterested = SafeList.string(e, "disinterestedRecommendations");
			disinterested.remove(contentUuid);
			interested.add(contentUuid);
			e.setUnindexedProperty("interestedRecommendations", interested);
			e.setUnindexedProperty("disinterestedRecommendations", disinterested);
			asyncDatastore.put(e);
		}
	}

	protected static void unmarkDisinterestedRecommendation(String userId, String contentUuid) {
		Entity e = getRecommendationsEntity(userId);
		List<String> disinterested = SafeList.string(e, "disinterestedRecommendations");
		int length = disinterested.size();
		disinterested.remove(contentUuid);
		if (disinterested.size() < length){
			e.setUnindexedProperty("disinterestedRecommendations", disinterested);
			asyncDatastore.put(e);
		}
	}
	
	protected static void unmarkInterestedRecommendation(String userId, String contentUuid) {
		Entity e = getRecommendationsEntity(userId);
		List<String> interested = SafeList.string(e, "interestedRecommendations");
		int length = interested.size();
		interested.remove(contentUuid);
		if (interested.size() < length){
			e.setUnindexedProperty("interestedRecommendations", interested);
			asyncDatastore.put(e);
		}
	}

	protected static List<String> refreshRecommendations(String userId){
		Entity e = refreshRecommendationsEntity(userId);
		List<String> recs = SafeList.string(e, "recommendations");
		List<String> hidden = SafeList.string(e, "hiddenRecommendations");
		recs.removeAll(hidden);
		return recs;
	}
	
	protected static List<String> getUsersInNeedOfRecalculation(int numToCalc) {
		Query q = new Query("UserRecommendations").addSort("updatedAt", SortDirection.ASCENDING);
		PreparedQuery newPQ = datastore.prepare(q);
		List<String> users = new ArrayList<String>();
		for(Entity e : newPQ.asIterable(FetchOptions.Builder.withLimit(numToCalc))){
			users.add((String) e.getKey().getName());
		}
		return users;
	}

	protected static void clearHidden(String userId) {
		Entity e = getRecommendationsEntity(userId);
		if (e.getProperty("hiddenRecommendations") == null) return;
		e.setUnindexedProperty("hiddenRecommendations", new ArrayList<String>());
		asyncDatastore.put(e);
	}

	protected static Entity getRecommendationsEntity(String userId){
		Key key = KeyFactory.createKey("UserRecommendations", userId);
		Entity e;
		try {
			e = datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			Entity refreshedRecommendations = refreshRecommendationsEntity(userId);
			return refreshedRecommendations;
		}
		Long lastUpdated = (Long) e.getProperty("updatedAt");
		if (lastUpdated == null) lastUpdated = new Long(0);
		
		long sinceLastUpdate = System.currentTimeMillis() - lastUpdated;
		if (sinceLastUpdate > updateInterval){
			Entity refreshedRecommendations = refreshRecommendationsEntity(userId);
			return refreshedRecommendations;
		} else {
			return e;
		}	
	}

	private static Entity refreshRecommendationsEntity(String userId){
		Key key = KeyFactory.createKey("UserRecommendations", userId);
		Entity e;
		try {
			e = datastore.get(key);
		} catch (EntityNotFoundException e1) {
			e = new Entity(key);
		}
		List<String> oldRecommendations = SafeList.string(e, "recommendations");
		RecommendationIndexAPI.updateUserRecommendations(userId);
		Map<String, Integer> mapping = new HashMap<String, Integer>();
		List<String> similarUsers = PhenotypeAPI.getSimilarUsers(userId, 20);
		for(String similarUser : similarUsers){
			List<String> bookmarks = BookmarksAPI.getUserBookmarkUuids(similarUser);
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
		
		int newRecommendations = 0;
		if (oldRecommendations == null){
			newRecommendations = refreshedRecommendations.size();
		} else {
			for (String s : refreshedRecommendations){
				if (! oldRecommendations.contains(s)){
					newRecommendations++;
				}
			}
		}
		e.setUnindexedProperty("unreadRecommendations", new Long(newRecommendations));
		e.setUnindexedProperty("recommendations", refreshedRecommendations);
		e.setProperty("updatedAt", System.currentTimeMillis());
		datastore.put(e);
		
		return e;
	}
	
	private static void increment(Map<String, Integer> map, String s) {
		if (map.containsKey(s)) {
			map.put(s, map.get(s) + 1);
		} else {
			map.put(s, 1);
		}
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

	protected static void markAllRecommendationsRead(String userId) {
		Entity entity = getRecommendationsEntity(userId);
		entity.setUnindexedProperty("unreadRecommendations", new Long(0));
		datastore.put(entity);	
	}
	
	protected static int getNumberOfUnreadRecommendations(String userId){
		Entity entity = getRecommendationsEntity(userId);
		Long l = (Long) entity.getProperty("unreadRecommendations");
		if (l == null) {
			List<String> recommendations = SafeList.string(entity, "recommendations");
			return recommendations.size();
		}
		return l.intValue();
	}
}
