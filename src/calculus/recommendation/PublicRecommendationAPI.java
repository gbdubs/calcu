package calculus.recommendation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.api.AchievementsAPI;
import calculus.api.ContentAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.utilities.MenuItem;
import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class PublicRecommendationAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<MenuItem> getUserRecommendations (String userId, int n) {
		if (!AchievementsAPI.hasUserPersonalized(userId)){
			return null;
		}
		Entity recEntity = MasterRecommendationsAPI.getRecommendationsEntity(userId);

		List<Text> jsonRecommendations = SafeList.text(recEntity, "jsonMenuItems");
		
		if (jsonRecommendations != null) {
			List<String> hidden = SafeList.string(recEntity, "hiddenRecommendations");
			List<String> interested = SafeList.string(recEntity, "interestedRecommendations");
			List<String> disinterested = SafeList.string(recEntity, "disinterestedRecommendations");
		
			List<MenuItem> results = new ArrayList<MenuItem>();
			for(int i = 0; results.size() < n && i < jsonRecommendations.size(); i++){
				String json = jsonRecommendations.get(i).getValue();
				MenuItem mi = MenuItem.fromJson(json);
				if (hidden == null || !hidden.contains(mi.getUuid())){
					String liked = null;
					if (interested != null && interested.contains(mi.getUuid())) {
						liked = "interested";
					} else if (disinterested != null && disinterested.contains(mi.getUuid())) {
						liked = "disinterested";
					}
					mi.withValue(liked);
					results.add(mi);
				}
			}
			return results;
			
		} else {
			List<MenuItem> recommendations = getUserRecommendationsManually(userId);
			
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
	}
	
	public static List<MenuItem> getUserRecommendationsManually (String userId) {
		List<MenuItem> result = new ArrayList<MenuItem>();
		List<String> contentUuids = MasterRecommendationsAPI.getRecommendations(userId);
		int maxPercentage = Math.min(Math.max(50, contentUuids.size()), 95);
		for(int i = 0; i < contentUuids.size(); i++){
			String uuid = contentUuids.get(i);
			try {
				Content content = ContentAPI.instantiateContent(uuid);
				String title = content.getTitle();
				String body = content.getAbbreviatedBody();
				String contentUrl = content.getUrl();
				String color = ContentAPI.getBoxColor(content.getContentType());
			
				int percentage = maxPercentage - i - (content.stableRandom(5) + 2);
				
				MenuItem mi = new MenuItem()
					.withPercentage(percentage + "%")
					.withColor(color)
					.withDescription(body)
					.withTitle(title)
					.withUuid(uuid)
					.withUrl(contentUrl);
	
				result.add(mi);
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
			List<MenuItem> defaultRecommendations = getLoggedOutRecommendations();
			req.setAttribute("recommendationsMenu", defaultRecommendations);
			req.setAttribute("unreadRecommendations", defaultRecommendations.size());
		} else {
			List<MenuItem> recommendations = getUserRecommendations(user.getUserId(), 50);
			req.setAttribute("recommendationsMenu", recommendations);
			req.setAttribute("unreadRecommendations", MasterRecommendationsAPI.getNumberOfUnreadRecommendations(user.getUserId()));
		}
	}

	private static List<MenuItem> getLoggedOutRecommendations() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		menuItems.add(new MenuItem()
			.withTitle("Using Big Data, we suggest")
			.withPercentage("80%")
			.withColor("aqua")
		);
		menuItems.add(new MenuItem()
			.withTitle("Content for you to check out.")
			.withPercentage("30%")
			.withColor("yellow")
		);
		menuItems.add(new MenuItem()
			.withTitle("We deduce your knowledge and needs")
			.withPercentage("50%")
			.withColor("green")
		);
		menuItems.add(new MenuItem()
			.withTitle("And direct you to the best resources!")
			.withPercentage("20%")
			.withColor("red")
		);
		return menuItems;
	}

	public static void markAllRecommendationsRead(String userId) {
		MasterRecommendationsAPI.markAllRecommendationsRead(userId);
	}

	public static void markDisinterested(String userId, String contentUuid) {
		MasterRecommendationsAPI.markDisinterestedRecommendation(userId, contentUuid);
	}
	
	public static void unmarkDisinterested(String userId, String contentUuid) {
		MasterRecommendationsAPI.unmarkDisinterestedRecommendation(userId, contentUuid);
	}

	public static void markInterested(String userId, String contentUuid) {
		MasterRecommendationsAPI.markInterestedRecommendation(userId, contentUuid);
	}
	
	public static void unmarkInterested(String userId, String contentUuid) {
		MasterRecommendationsAPI.unmarkInterestedRecommendation(userId, contentUuid);
	}
}
