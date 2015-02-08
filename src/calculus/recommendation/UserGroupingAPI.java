package calculus.recommendation;

import java.util.List;

import calculus.api.BookmarksAPI;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UserGroupingAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<String> getUsersWithPhenotype(String phenotype){
		
		
		return null;
	}
	
	public static List<String> getUserBookmarks(String userId){
		return BookmarksAPI.getUserBookmarks(userId);
	}
	
	public static List<String> getRecommendationsForUser(String userId){
	
		
		
		
	}
	
}
