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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;

public class PublicRecommendationAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<MenuItem> getUserRecommendations (String userId, int n) {
		List<MenuItem> result = new ArrayList<MenuItem>();
		List<String> contentUuids = MasterRecommendationsAPI.getRecommendations(userId);
		int maxPercentage = Math.min(Math.max(50, contentUuids.size()), 95);
		for(int i = 0; i < contentUuids.size() && i < n; i++){
			String uuid = contentUuids.get(i);
			try {
				Content content = new Content(uuid);
				String title = content.getTitle();
			
				String contentUrl = content.getUrl();
				String color = ContentAPI.getBoxColor(content.getContentType());
			
				int percentage = maxPercentage - i - (content.stableRandom(5) + 2);
				
				result.add(new MenuItem(contentUrl, uuid, title, "", "", percentage + "%", color, "", ""));
			} catch (EntityNotFoundException e) {
				// Don't worry, don't include. Skip
			}
		}
		return result;
	}
	
	public static void dismissRecommendation(String userId, String contentUuid){
		MasterRecommendationsAPI.removeRecommendation(userId, contentUuid);
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
