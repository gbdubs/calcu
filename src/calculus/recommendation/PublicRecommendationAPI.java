package calculus.recommendation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.api.ContentAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.utilities.MenuItem;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class PublicRecommendationAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<MenuItem> getUserRecommendations (String userId, int n) {
		Key key = KeyFactory.createKey("Recommendations", userId);
		Entity recEntity;
		try {
			recEntity = datastore.get(key);
			List<Text> jsonRecommendations = (List<Text>) recEntity.getProperty("jsonMenuItems");
			List<String> hidden = (List<String>) recEntity.getProperty("hiddenRecommendations");
			if (jsonRecommendations != null){
				List<MenuItem> results = new ArrayList<MenuItem>();
				for(int i = 0; results.size() < n && i < jsonRecommendations.size(); i++){
					String json = jsonRecommendations.get(i).getValue();
					MenuItem mi = MenuItem.fromJson(json);
					if (hidden == null || !hidden.contains(mi.getUuid())){
						results.add(mi);
					}
				}
				return results;
			}
		} catch (EntityNotFoundException e) {
			
		}
		List<MenuItem> recommendations = getUserRecommendationsManually(userId);
		try {
			recEntity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			recEntity = new Entity(key);
		}
		List<Text> jsonRepresentations = new ArrayList<Text>();
		for(MenuItem mi : recommendations){
			jsonRepresentations.add(new Text(mi.toJson()));
		}
		recEntity.setUnindexedProperty("jsonMenuItems", jsonRepresentations);
		datastore.put(recEntity);
		
		List<MenuItem> results = new ArrayList<MenuItem>();
		for(int i = 0; i < n && i < recommendations.size(); i++){
			results.add(recommendations.get(i));
		}
		
		return results;
	}
	
	public static List<MenuItem> getUserRecommendationsManually (String userId) {
		List<MenuItem> result = new ArrayList<MenuItem>();
		List<String> contentUuids = MasterRecommendationsAPI.getRecommendations(userId);
		int maxPercentage = Math.min(Math.max(50, contentUuids.size()), 95);
		for(int i = 0; i < contentUuids.size(); i++){
			String uuid = contentUuids.get(i);
			try {
				Content content = new Content(uuid);
				String title = content.getTitle();
				String body = content.getAbbreviatedBody();
				String karma = content.getKarma();
				String contentUrl = content.getUrl();
				String color = ContentAPI.getBoxColor(content.getContentType());
			
				int percentage = maxPercentage - i - (content.stableRandom(5) + 2);
				
				result.add(new MenuItem(contentUrl, uuid, title, body, "", percentage + "%", color, "", ""));
			} catch (EntityNotFoundException e) {
				// Don't worry, don't include. Skip
			}
		}
		return result;
	}
	
	public static void dismissRecommendation(String userId, String contentUuid){
		MasterRecommendationsAPI.hideRecommendation(userId, contentUuid);
	}
	
	public static void resetHiddenRecommendations(String userId){
		MasterRecommendationsAPI.clearHidden(userId);
	}
	
	public static PracticeProblem getDifficultyCalibrationPracticeProblem(String userId) {
		return PracticeProblemAPI.getAnswerablePracticeProblem(userId);
	}

	public static Question getDifficultyCalibrationQuestion(String userId) {
		return QuestionAPI.getAnswerableQuestion(userId);
	}

	public static void refreshRecommendations(String userId) {
		MasterRecommendationsAPI.refreshRecommendations(userId);
	}

	public static List<String> getUsersInNeedOfRecalculation(int numToCalc) {
		return MasterRecommendationsAPI.getUsersInNeedOfRecalculation(numToCalc);
	}

	public static void addUserRecommendationsToRequest(HttpServletRequest req, User user) {
		if (user == null){
			MenuItem[] recommendations = new MenuItem[4];
			recommendations[0] = new MenuItem("#", "", "Using Big Data, we suggest", "", "", "80%", "aqua", "", "");
			recommendations[1] = new MenuItem("#", "", "Content for you to check out.", "", "", "30%", "yellow", "", "");
			recommendations[2] = new MenuItem("#", "", "We deduce your knowledge and needs", "", "", "50%", "green", "", "");
			recommendations[3] = new MenuItem("#", "", "And direct you to the best resources!", "", "", "20%", "red", "", "");
			req.setAttribute("recommendationsMenu", recommendations);
		} else {
			List<MenuItem> recommendations = getUserRecommendations(user.getUserId(), 50);
			req.setAttribute("recommendationsMenu", recommendations);
		}
	}
}
