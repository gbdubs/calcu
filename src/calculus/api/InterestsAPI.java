package calculus.api;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InterestsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	private static Entity getUserInterestsProfile(String userId){
		Key key = KeyFactory.createKey("UserInterests", userId);
		Entity result;
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			result = new Entity(key);
			String[] defaultInterests = {
					"calculus", "linear algebra", "differential equations", "derivatives", "integrals", "related rates", "integration by parts", "geometry", "algebra", "trigonometry", "trig"
			};
			List<String> defaults = new ArrayList<String>();
			for (String s : defaultInterests){defaults.add(s);}
			result.setProperty("updatedOn", System.currentTimeMillis());
			result.setUnindexedProperty("interests", defaults);
			result.setUnindexedProperty("popularTagOffset", 0);
			return result;
		}
	}
	
	public static void appendNewInterests(String userId, List<String> newInterests){
		Entity interests = getUserInterestsProfile(userId);
		List<String> list = getInterests(interests);
		if (list == null) list = new ArrayList<String>();
		for (String s : newInterests){
			if (!list.contains(s)){
				list.add(s);
			}
		}
		interests.setUnindexedProperty("interests", list);
		interests.setProperty("updatedOn", System.currentTimeMillis());
		datastore.put(interests);
	}
	
	public static void appendNewInterest(String userId, String newInterest){
		Entity interests = getUserInterestsProfile(userId);
		List<String> list = getInterests(interests);
		if (! list.contains(newInterest)) {
			list.add(newInterest);
		}
		interests.setUnindexedProperty("interests", list);
		interests.setProperty("updatedOn", System.currentTimeMillis());
		datastore.put(interests);
	}
	
	public static List<String> getInterests(String userId){
		Entity interests = getUserInterestsProfile(userId);
		return getInterests(interests);
	}
	
	private static List<String> getInterests(Entity userInterestsProfile){
		List<String> list = (List<String>) userInterestsProfile.getProperty("interests");
		if (list == null) list = new ArrayList<String>();
		return list;
	}
	
	public static List<String> getAndCycleFirstNInterests(String userId, int n){
		if (n < 1) return null;
		Entity interests = getUserInterestsProfile(userId);
		List<String> list = getInterests(interests);
		interests.setProperty("updatedOn", System.currentTimeMillis());
		if (n >= list.size()){
			return list;
		} else {
			List<String> toReturn = list.subList(0, n);
			List<String> toKeep = list.subList(n, list.size());
			// Adds the values that will be returned to the end of the list so that they dont' show up next time.
			toKeep.addAll(toReturn);
			interests.setUnindexedProperty("interests", toKeep);
			datastore.put(interests);
			return toReturn;
		}
	}
	
	public static List<String> getPotentialInterests(String userId, int maxResults){
		Entity interestProfile = getUserInterestsProfile(userId);
		List<String> currentInterests = getInterests(interestProfile);
		
		int offset = intValue(interestProfile, "popularTagOffset");
		interestProfile.setProperty("popularTagOffset", offset + maxResults);
		
		List<String> popularTags = TagAPI.getPopularTags(maxResults + 10, offset);
		popularTags.removeAll(currentInterests);

		if (popularTags == null || popularTags.size() < maxResults){
			interestProfile.setProperty("popularTagOffset", 0);
			return popularTags;
		}
		datastore.put(interestProfile);
		
		System.out.println("PopularTags: " +popularTags.toString());
		System.out.println("CurrentTags: " +currentInterests);

		return popularTags;
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

	public static void removeInterests(String userId, List<String> removingTags) {
		Entity interests = getUserInterestsProfile(userId);
		List<String> list = getInterests(interests);
		list.removeAll(removingTags);
		interests.setUnindexedProperty("interests", list);
		interests.setProperty("updatedOn", System.currentTimeMillis());
		datastore.put(interests);
	}
}
