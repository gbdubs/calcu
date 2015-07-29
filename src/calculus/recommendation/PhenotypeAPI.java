package calculus.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculus.api.RandomValuesAPI;
import calculus.api.UserPublicInfoAPI;
import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PhenotypeAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static final int phenotypeLength = 13;
	public static final String DEFAULT_PHENOTYPE = "-------------";	
	
	protected static List<String> getSimilarUsers(String userId, int n) {
		String phenotype = UserPublicInfoAPI.getPhenotype(userId);
		
		List<String> randomPhenotypes = RandomValuesAPI.getRandomPhenotypes(n * 4);
		
		Map<String, Float> mapping = new HashMap<String, Float>();
		for(String randPhenotype : randomPhenotypes){
			float comparison = evaluatePhenotypeComparison(phenotype, randPhenotype);
			List<String> userIds = getUsersWithPhenotype(randPhenotype);
			for (String uid : userIds) {
				if (mapping.containsKey(uid)) {
					float value = Math.max(comparison, mapping.get(uid));
					mapping.put(uid, value);
				} else {
					mapping.put(uid, comparison);
				}
			}
		}
		
		List<String> similarUsers = getDecendingListFromCountMap(mapping);
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < similarUsers.size() && i < n; i++){
			result.add(similarUsers.get(i));
		}
		return result;
	}
	
	private static Entity getPhenotypeEntity(String phenotype){
		Key key = KeyFactory.createKey("Phenotype", phenotype);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			Entity result = new Entity (key);
			result.setUnindexedProperty("users", new ArrayList<String>());
			return result;
		}
	}
	
	private static void setUserPhenotype(String userId, String newPhenotype, String oldPhenotype){
		if (oldPhenotype != ""){
			Entity oldPhenotypeEntity = getPhenotypeEntity(oldPhenotype);
			removeUserFromPhenotypeEntity(oldPhenotypeEntity, userId);
		}
		UserPublicInfoAPI.setUserPhenotype(userId, newPhenotype);
		Entity newPhenotypeEntity = getPhenotypeEntity(newPhenotype);
		addUserToPhenotypeEntity(newPhenotypeEntity, userId);
	}
	
	private static void addUserToPhenotypeEntity(Entity pEntity, String userId){
		List<String> users = SafeList.string(pEntity, "users");
		if (! users.contains(userId)){
			users.add(userId);
			pEntity.setUnindexedProperty("users", users);
			asyncDatastore.put(pEntity);
		}
	}
	
	// WE WILL ONLY DO THIS IF WE GET A LOT OF USERS. This way, there will be more people in each type, and 
	// we can still provide suggestions to the uninitiated. 
	private static void removeUserFromPhenotypeEntity(Entity pEntity, String userId){
		/*
		 * List<String> users = (List<String>) pEntity.getProperty("users");
		 * if (users == null) return;
		 * int size = users.size();
		 * users.remove(userId);
		 * if (users.size() != size){
		 * 		pEntity.setUnindexedProperty("users", users);
		 * 		datastore.put(pEntity);
		   }*/	
	}
	
	private static List<String> getUsersWithPhenotype(String phenotype){
		Entity phenotypeEntity = getPhenotypeEntity(phenotype);
		List<String> userIds = SafeList.string(phenotypeEntity, "users");
		if (userIds == null) return new ArrayList<String>();
		return userIds;
	}
	
	private static float evaluatePhenotypeComparison(String p1, String p2){
		int totalSimilarity = 0;
		for(int i = 0; i < phenotypeLength; i++){
			totalSimilarity += getSimilarity(p1.charAt(i), p2.charAt(i));
		}
		return (float) Math.pow((float) totalSimilarity / phenotypeLength, 2);
	}
	
	public static float evaluateUserComparison(String userId1, String userId2){
		String p1 = correctLength(UserPublicInfoAPI.getPhenotype(userId1));
		String p2 = correctLength(UserPublicInfoAPI.getPhenotype(userId2));
		return evaluatePhenotypeComparison(p1, p2);
	}
	
	private static int getSimilarity(char c1, char c2){
		if (c1 == c2) return 10;
		if (c1 == '-' && c2 == '-') return 8;
		if (c1 == '-' || c2 == '-') return 5;
		return 0;
	}
	
	public static int whatQuestionToAskUser(String userId){
		String phenotype = UserPublicInfoAPI.getPhenotype(userId);
		phenotype = correctLength(phenotype);
		for (int i = 0; i < phenotypeLength; i++){
			if (phenotype.charAt(i) == '-'){
				return i;
			}
		}
		return -1;
	}

	private static String correctLength(String p) {
		p = p.trim();
		if (p.length() > phenotypeLength){
			p = p.substring(0, phenotypeLength);
		}
		if (p.length() < phenotypeLength){
			while (p.length() < phenotypeLength){
				p = p + "-";
			}
		}
		return p;
	}

	public static void updateUserPhenotype(String userId, char preferenceChar) {
		String oldPhenotype = UserPublicInfoAPI.getPhenotype(userId);
		int location = ("" + preferenceChar).toUpperCase().charAt(0) - 65;
		String start = oldPhenotype.substring(0, location);
		String end = oldPhenotype.substring(location+1);
		String newPhenotype = start + preferenceChar + end;
		setUserPhenotype(userId, newPhenotype, oldPhenotype);
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
