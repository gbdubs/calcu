package calculus.api;

import java.util.HashMap;
import java.util.Map;

public class RecommendationsAPI {

	public static boolean[] temp = new boolean[24];
	
	public static Map<String, Boolean> getUserInterestPossibilities(String userId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		for(int i = 0; i < 24; i++){
			result.put("" + i, temp[i]);
		}
		
		return result;
	}

	public static void addInterest(String interest, String userId) {
		int i = Integer.parseInt(interest);
		temp[i] = true;
		
	}

	public static void removeInterest(String interest, String userId) {
		int i = Integer.parseInt(interest);
		temp[i] = false;
	}

}
