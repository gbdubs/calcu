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
	
	public static List<String> getPotentialInterests(String userId, int maxResults){
		Entity interestProfile = getUserInterestsProfile(userId);
		List<String> currentInterests = getInterests(interestProfile);
		
		int offset = intValue(interestProfile, "popularTagOffset");
		interestProfile.setProperty("popularTagOffset", offset + maxResults);
		
		List<String> popularTags = TagAPI.getPopularTags(maxResults, offset);
		popularTags.removeAll(currentInterests);

		if (popularTags == null || popularTags.size() < maxResults){
			interestProfile.setProperty("popularTagOffset", 0);
		}
		
		datastore.put(interestProfile);

		return popularTags;
	}

	public static List<String> getAndCycleFirstNInterests(String userId, int n){
		if (n < 1) return null;
		Entity interestsProfile = getUserInterestsProfile(userId);
		List<String> interests = getInterests(interestsProfile);
		interestsProfile.setProperty("updatedOn", System.currentTimeMillis());
		if (n >= interests.size()){
			return interests;
		} else {
			int length = interests.size();
			List<String> toReturn = sublist(interests, 0, n);
			List<String> toKeep = sublist(interests, n, length);
			// Adds the values that will be returned to the end of the list so that they dont' show up next time.
			toKeep.addAll(toReturn);
			interestsProfile.setUnindexedProperty("interests", toKeep);
			datastore.put(interestsProfile);
			return toReturn;
		}
	}
	
	public static void appendNewInterests(String userId, List<String> newInterests){
		Entity interestsProfile = getUserInterestsProfile(userId);
		List<String> list = getInterests(interestsProfile);
		if (list == null) list = new ArrayList<String>();
		for (String s : newInterests){
			if (!list.contains(s)){
				list.add(s);
			}
		}
		interestsProfile.setUnindexedProperty("interests", list);
		interestsProfile.setProperty("updatedOn", System.currentTimeMillis());
		datastore.put(interestsProfile);
	}

	public static void appendNewInterest(String userId, String newInterest){
		Entity interestsProfile = getUserInterestsProfile(userId);
		List<String> list = getInterests(interestsProfile);
		if (! list.contains(newInterest)) {
			list.add(newInterest);
		}
		interestsProfile.setUnindexedProperty("interests", list);
		interestsProfile.setProperty("updatedOn", System.currentTimeMillis());
		datastore.put(interestsProfile);
	}

	public static void removeInterests(String userId, List<String> removingTags) {
		Entity interestsProfile = getUserInterestsProfile(userId);
		List<String> list = getInterests(interestsProfile);
		list.removeAll(removingTags);
		interestsProfile.setUnindexedProperty("interests", list);
		interestsProfile.setProperty("updatedOn", System.currentTimeMillis());
		datastore.put(interestsProfile);
	}

	private static Entity getUserInterestsProfile(String userId){
		Key key = KeyFactory.createKey("UserInterests", userId);
		Entity interestsProfile;
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			interestsProfile = new Entity(key);
			String[] defaultInterests = {
					"calculus", "linear algebra", "differential equations", "derivatives", "integrals", "related rates", "integration by parts", "geometry", "algebra", "trigonometry", "trig"
			};
			List<String> defaults = new ArrayList<String>();
			for (String s : defaultInterests){defaults.add(s);}
			interestsProfile.setProperty("updatedOn", System.currentTimeMillis());
			interestsProfile.setUnindexedProperty("interests", defaults);
			interestsProfile.setUnindexedProperty("popularTagOffset", 0);
			return interestsProfile;
		}
	}

	public static List<String> getInterests(String userId){
		Entity interestsProfile = getUserInterestsProfile(userId);
		return getInterests(interestsProfile);
	}

	private static List<String> getInterests(Entity userInterestsProfile){
		List<String> interests = (List<String>) userInterestsProfile.getProperty("interests");
		if (interests == null) interests = new ArrayList<String>();
		return interests;
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

	private static List<String> sublist(List<String> list, int start, int stop){
		List<String> result = new ArrayList<String>();
		for(int i = start; i < stop; i++){
			result.add(list.get(i));
		}
		return result;
	}

}
