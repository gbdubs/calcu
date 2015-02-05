package calculus.api;

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

public class TagAPI {

	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	
	public static void addNewContentToTag(String contentUuid, String tag){
		String cleanedTag = tag.toLowerCase().trim();
		Key key = KeyFactory.createKey("Tag", cleanedTag);
		Entity entity = new Entity(key);
		List<String> uuids = new ArrayList<String>();
		try {
			entity = datastoreService.get(key);
			uuids = (List<String>) entity.getProperty("matchingContent");
		} catch (EntityNotFoundException e) {
			entity.setProperty("name", cleanedTag);
		}
		uuids.add(contentUuid);
		entity.setUnindexedProperty("matchingContent", uuids);
		datastoreService.put(entity);
	}

	public static List<String> getUuidsResultsOfMultipleTags(String[] tags, int maxNumResults, int seed){
		List<String> tagsList = new ArrayList<String>();
		for(String t : tags){
			tagsList.add(t);
		}
		return getUuidsResultsOfMultipleTags(tagsList, maxNumResults, seed);
	}
	
	public static List<String> getUuidsResultsOfMultipleTags(List<String> tags, int maxNumResults, int seed){
		Map<String, Integer> mapping = new HashMap<String, Integer>();
		for (String tag : tags){
			String cleanedTag = tag.toLowerCase().trim();
			List<String> uuids = getUuidsOfTag(cleanedTag);
			for(String uuid : uuids){
				if (mapping.containsKey(uuid)){
					mapping.put(uuid, mapping.get(uuid) + 1);
				} else {
					mapping.put(uuid, 1);
				}
			}
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
			int fromIndex = (seed - 1) * maxNumResults;
			int toIndex = Math.min(uuids.size(), fromIndex + maxNumResults);
			uuids = uuids.subList(fromIndex, toIndex);
		}
		
		return uuids;
	}

	public static List<String> getUuidsOfTag(String tag){
		Key key = KeyFactory.createKey("Tag", tag);
		Entity entity;
		try {
			entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			return new ArrayList<String>();
		}
		return (List<String>) entity.getProperty("matchingContent");
	}
}
