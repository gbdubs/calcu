package calculus.recommendation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import calculus.api.UserPublicInfoAPI;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PhenotypeAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static final int phenotypeLength = 10;
	public static final String DEFAULT_PHENOTYPE = "----------";	
	
	public static Set<String> getSimilarUsers(String userId, int n){
		String phenotype = UserPublicInfoAPI.getPhenotype(userId);
		Set<String> userIds = new HashSet<String>();
		List<String> usersWithSamePhenotype = getUsersWithPhenotype(phenotype);
		int i = 0; 
		while (userIds.size() < n && i < usersWithSamePhenotype.size()){
			userIds.add(usersWithSamePhenotype.get(i));
		}
		i = 0;
		while(userIds.size() < n){
			List<String> similarPhenotypes = similarPhenotypes(phenotype, 10 * (i+1));
			int j = 0;
			while (userIds.size() < n && j < 10){
				int index = j + 10 * i;
				usersWithSamePhenotype = getUsersWithPhenotype(similarPhenotypes.get(index));
				int k = 0;
				while (userIds.size() < n && k < usersWithSamePhenotype.size()){
					userIds.add(usersWithSamePhenotype.get(k));
				}
			}
		}
		return userIds;
	}
	
	private static Entity getPhenotypeEntity(String phenotype){
		Key key = KeyFactory.createKey("PhenotypeAPI", phenotype);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			Entity result = new Entity (key);
			result.setProperty("users", new ArrayList<String>());
			return result;
		}
	}
	
	public static void setUserPhenotype(String userId, String newPhenotype){
		setUserPhenotype(userId, newPhenotype, "");
	}
	
	public static void setUserPhenotype(String userId, String newPhenotype, String oldPhenotype){
		if (oldPhenotype != ""){
			Entity oldPhenotypeEntity = getPhenotypeEntity(oldPhenotype);
			removeUserFromPhenotypeEntity(oldPhenotypeEntity, userId);
		}
		Entity newPhenotypeEntity = getPhenotypeEntity(newPhenotype);
		addUserToPhenotypeEntity(newPhenotypeEntity, userId);
	}
	
	private static void addUserToPhenotypeEntity(Entity pEntity, String userId){
		List<String> users = (List<String>) pEntity.getProperty("users");
		if (users == null) users = new ArrayList<String>();
		if (! users.contains(userId)) users.add(userId);
		pEntity.setProperty("users", users);
		datastore.put(pEntity);
	}
	
	private static void removeUserFromPhenotypeEntity(Entity pEntity, String userId){
		List<String> users = (List<String>) pEntity.getProperty("users");
		if (users == null) return;
		int size = users.size();
		users.remove(userId);
		if (users.size() != size){
			pEntity.setProperty("users", users);
			datastore.put(pEntity);
		}	
	}
	
	private static List<String> getUsersWithPhenotype(String phenotype){
		Entity phenotypeEntity = getPhenotypeEntity(phenotype);
		List<String> userIds = (List<String>) phenotypeEntity.getProperty("users");
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
	
	public static int whatQuestionToAsk(String phenotype){
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

	private static List<String> similarPhenotypes(String phenotype, int n){
		List<String> results = new ArrayList<String>();
		int i = 0;
		while (results.size() < n){
			results.add(permutePhenotype(phenotype, i, true));
			results.add(permutePhenotype(phenotype, i, false));	
			i++;	
			if (i == phenotypeLength){
				i = 0; 
				phenotype = results.get((int) (Math.random() * i));
			}
		}
		return results;
	}

	private static String permutePhenotype(String phenotype, int i, boolean uppercase){
		char c = '-';
		if (uppercase){
			c = (char) (i + 65);
		} else {
			c = (char) (i + 97);
		}
		String start = phenotype.substring(0,i); 
		String end = phenotype.substring(i+1);
		return start + c + end;
	}
	
}
