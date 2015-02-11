package calculus.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import calculus.api.BookmarksAPI;

public class UserGroupingAPI {
	
	public static List<String> getPhenotypeRecommendationsForUser(String userId){
		Set<String> similarUsers = PhenotypeAPI.getSimilarUsers(userId, 10);
		List<String> otherUsersBookmarks = getUsersBookmarks(similarUsers);
		return otherUsersBookmarks;
	}
	
	private static List<String> getUsersBookmarks(Collection<String> userIds){
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String userId : userIds){
			List<String> userBookmarks = BookmarksAPI.getUserBookmarks(userId);
			for (String bookmark : userBookmarks){
				if (result.containsKey(bookmark)){
					result.put(bookmark, result.get(bookmark) + 1);
				} else {
					result.put(bookmark, 1);
				}
			}
		}
		List<String> mostFrequentBookmarks = getDecendingListFromCountMap(result);
		
		return mostFrequentBookmarks;
	}

	private static List<String> getDecendingListFromCountMap(Map<String, Integer> mapping){
		List<String> result = new ArrayList<String>();
		for(String str : mapping.keySet()){
			int i = 0;
			int value = mapping.get(str);
			while (i < result.size() && mapping.get(result.get(i)) <= value){
				i++;
			}
			result.add(i, str);
		}
		return result;
	}

	public static List<String> getNSimilarUsers(String userId, int n) {
		Set<String> similarUsers = PhenotypeAPI.getSimilarUsers(userId, n);
		return new ArrayList<String>(similarUsers);
	}
}
