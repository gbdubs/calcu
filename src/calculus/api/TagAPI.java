package calculus.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import calculus.utilities.LevenshteinDistance;
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

public class TagAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static final String allTagsKey = "lkq3jneoxi2398rskdnf2039uxldkf093";
	
	public static void addNewContentToTag(String contentUuid, String tag){
		String cleanedTag = tag.toLowerCase().trim();
		if (!acceptableTag(cleanedTag)){
			return;
		}
		Key key = KeyFactory.createKey("Tag", cleanedTag);
		Entity entity = new Entity(key);
		List<String> uuids = new ArrayList<String>();
		try {
			entity = datastore.get(key);
			uuids = SafeList.string(entity, "matchingContent");
		} catch (EntityNotFoundException e) {
			entity.setUnindexedProperty("name", cleanedTag);
			addNewTagToAllTags(cleanedTag);
		}
		if (!uuids.contains(contentUuid)){
			uuids.add(contentUuid);
			entity.setProperty("count", uuids.size());
			entity.setUnindexedProperty("matchingContent", uuids);
			asyncDatastore.put(entity);
		}
	}

	public static void removeContentFromTag(String uuid, String tag) {
		String cleanedTag = tag.toLowerCase().trim();
		if (!acceptableTag(cleanedTag)){
			return;
		}
		Key key = KeyFactory.createKey("Tag", cleanedTag);
		Entity entity = new Entity(key);
		List<String> uuids = new ArrayList<String>();
		try {
			entity = datastore.get(key);
			uuids = SafeList.string(entity, "matchingContent");
		} catch (EntityNotFoundException e) {
			return;
		}
		if (uuids.contains(uuid)){
			uuids.remove(uuid);
			entity.setProperty("count", uuids.size());
			entity.setUnindexedProperty("matchingContent", uuids);
			datastore.put(entity);
		}
	}

	public static void addNewTopicToTag(String topicUuid, String tag){
		String cleanedTag = tag.toLowerCase().trim();
		if (!acceptableTag(cleanedTag)){
			return;
		}
		Key key = KeyFactory.createKey("Tag", cleanedTag);
		Entity entity = new Entity(key);
		List<String> topicUuids;
		try {
			entity = datastore.get(key);
			topicUuids = SafeList.string(entity, "matchingTopics");
		} catch (EntityNotFoundException e) {
			entity.setUnindexedProperty("name", cleanedTag);
			addNewTagToAllTags(cleanedTag);
			topicUuids = new ArrayList<String>();
		}
		if (!topicUuids.contains(topicUuid)){
			topicUuids.add(topicUuid);
			entity.setProperty("topicCount", topicUuids.size());
			entity.setUnindexedProperty("matchingTopics", topicUuids);
			datastore.put(entity);
		}
	}
	
	
	public static void removeTopicFrimTag(String topicUuid, String tag){
		String cleanedTag = tag.toLowerCase().trim();
		Key key = KeyFactory.createKey("Tag", cleanedTag);
		Entity entity = new Entity(key);
		List<String> topicUuids = new ArrayList<String>();
		try {
			entity = datastore.get(key);
			topicUuids = SafeList.string(entity, "matchingTopics");
		} catch (EntityNotFoundException e) {
			return;
		}
		if (topicUuids.contains(topicUuid)){
			topicUuids.remove(topicUuid);
			entity.setProperty("topicCount", topicUuids.size());
			entity.setUnindexedProperty("matchingTopics", topicUuids);
			datastore.put(entity);
		}
	}
	
	private static void addNewTagToAllTags(String tag){
		Key key = KeyFactory.createKey("Tag", allTagsKey);
		Entity e;
		try {
			e = datastore.get(key);
		} catch (EntityNotFoundException enfe) {
			e = new Entity(key);
		}
		
		List<String> allTags = SafeList.string(e, "allTags");
		
		if (!allTags.contains(tag)){
			allTags.add(tag);
			e.setUnindexedProperty("allTags", allTags);
			asyncDatastore.put(e);
		}
	}
	
	private static List<String> getAllTags() {
		Key key = KeyFactory.createKey("Tag", allTagsKey);
		Entity e;
		try {
			e = datastore.get(key);
			List<String> allTags = SafeList.string(e, "allTags");
			return allTags;
		} catch (EntityNotFoundException enfe) {
			return new ArrayList<String>();
		}
	}
	
	public static List<String> getUuidsResultsOfMultipleTags(String tags, int maxNumResults, int seed){
		List<String> tagsList = getTagsFromString(tags);
		return getUuidsResultsOfMultipleTags(tagsList, maxNumResults, seed);
	}
	
	public static List<String> getUuidsResultsOfMultipleTags(String[] tags, int maxNumResults, int seed){
		List<String> tagsList = new ArrayList<String>();
		for(String t : tags){
			tagsList.add(t);
		}
		return getUuidsResultsOfMultipleTags(tagsList, maxNumResults, seed);
	}

	// Cost = N READ (Where N is the number of Tags)
	public static List<String> getUuidsResultsOfMultipleTags(List<String> tags, int maxNumResults, int seed){
		Map<String, Integer> mapping = new HashMap<String, Integer>();
		List<Future<Entity>> futures = new ArrayList<Future<Entity>>();
		for (String tag : tags){
			String cleanedTag = tag.toLowerCase().trim();
			if (!cleanedTag.equals("")){
				Future<Entity> future = asyncDatastore.get(KeyFactory.createKey("Tag", tag));
				futures.add(future);
			}
		}
		
		for(Future<Entity> future : futures) {
			Entity e;
			try {
				e = future.get();
				List<String> uuids = SafeList.string(e, "matchingContent");
				for(String uuid : uuids){
					if (mapping.containsKey(uuid)){
						mapping.put(uuid, mapping.get(uuid) + 1);
					} else {
						mapping.put(uuid, 1);
					}
				}
			} catch (InterruptedException | ExecutionException e1) {/* Skip and don't include.*/}
		}
		List<String> uuids = new ArrayList<String>();
		for(String uuid : mapping.keySet()){
			int i = 0;
			int value = mapping.get(uuid);
			while (i < uuids.size() && mapping.get(uuids.get(i)) <= value){
				i++;
			}
			uuids.add(i, uuid);
		}
		
		if (uuids.size() > maxNumResults){
			int fromIndex = seed * maxNumResults;
			int toIndex = Math.min(uuids.size(), fromIndex + maxNumResults);
			uuids = uuids.subList(fromIndex, toIndex);
		}
		
		return uuids;
	}

	public static List<String> getPopularTags(int num, int offset) {
		Query q = new Query("Tag").addSort("count", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(num).offset(offset));
		List<String> tags = new ArrayList<String>();
		for(Entity e : entities){
			tags.add((String) e.getProperty("name"));
		}
		return tags;
	}
	
	public static List<String> getTagsFromString(String tags){
		List<String> allTags = new ArrayList<String>();
		if (tags == null || tags.length() == 0) return allTags;
		String[] tagsList = tags.split(",");
		for(String tag : tagsList){
			if (!tag.equals("")){
				allTags.add(tag);
			}
		}
		return allTags;
	}
	
	public static List<String> getSimilarTags(String original){
		List<String> allTags = getAllTags();
		return calculateSimilarTags(original, allTags, 5);
	}

	private static boolean acceptableTag (String tag) {
		if (tag.length() > 50 || tag.length() < 3){
			return false;
		}
		String[] unacceptableStrings = {"and", "of", "or", "as", "at", "in", "on"};
		List<String> unacceptable = Arrays.asList(unacceptableStrings);
		if (unacceptable.contains(tag)){
			return false;
		}
		int alphanumeric = 0;
		int other = 0;
		char[] chars = tag.toCharArray();
		for(char c : chars){
			if (alphanumeric(c)) alphanumeric++;
			else other++;
		}
		if (other > 5 || other >= 2 * alphanumeric){
			return false;
		} else {
			return true;
		}
	}
	
	private static boolean alphanumeric (char c) {
		int i = (int) c;
		if (65 <= i && i <= 90) {
			return true;
		} else if (48 <= i && i <= 57) {
			return true;
		} else if (97 <= i && i <= 122) {
			return true;
		}
		return false;
	}
	
	private static List<String> calculateSimilarTags(String tag1, List<String> allTags, int numberToReturn){
		Map<Float, String> mapping = new HashMap<Float, String>();
		List<Float> allValues = new ArrayList<Float>();
		for (String tag2 : allTags) {
			float pairwiseDifference = pairwiseDifference(tag1, tag2);
			mapping.put(pairwiseDifference, tag2);
			allValues.add(pairwiseDifference);
		}
		List<String> similarTags = new ArrayList<String>();
		Collections.sort(allValues);
		int i = -1;
		while (++i < allValues.size() && similarTags.size() < numberToReturn){
			similarTags.add(mapping.get(allValues.get(i)));
		}
		return similarTags;
	}

	private static float pairwiseDifference(String tag1, String tag2) {
		return (float) (LevenshteinDistance.computeLevenshteinDistance(tag1, tag2) + Math.random());
	}
}
