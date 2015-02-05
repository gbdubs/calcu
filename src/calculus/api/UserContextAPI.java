package calculus.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Content;
import calculus.utilities.KarmaDescription;
import calculus.utilities.MenuItem;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class UserContextAPI {
	
	private static UserService userService = UserServiceFactory.getUserService();
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void addUserContextToRequest(HttpServletRequest req, String pageUrl){
		
		User user = userService.getCurrentUser();
		
		String loginUrl = userService.createLoginURL(pageUrl);
		String logoutUrl = userService.createLogoutURL(pageUrl);
		
		req.setAttribute("user", user);
		req.setAttribute("loginUrl", loginUrl);
		req.setAttribute("logoutUrl", logoutUrl);

		addUserPublicInfoToRequest(req, user);
		BookmarksAPI.addUserBookmarksToRequest(req, user);
		NotificationsAPI.addUserNotificationsToRequest(req, user);
		addUserRecommendationsToRequest(req, user);
	}
	
	private static void addUserPublicInfoToRequest(HttpServletRequest req, User user){
		
		String username = "Anonymous Elephant";
		int karma = 0; 
		int level = 1;
		String profilePictureUrl = "/_static/img/elephant.png";
		String profileUrl = "/";
		String email = "anonymous314159@gmail.com";
		
		if (user != null){
			Entity publicInfo = UserPublicInfoAPI.getOrCreateMyPublicInfo(user);
			karma = ((Long) publicInfo.getProperty("karma")).intValue();
			level = ((Long) publicInfo.getProperty("level")).intValue();
			username = (String) publicInfo.getProperty("username");
			profilePictureUrl = (String) publicInfo.getProperty("profilePictureUrl");
			profileUrl = (String) publicInfo.getProperty("profileUrl");
			email = (String) publicInfo.getProperty("email");
			
			int calculatedLevel = KarmaAPI.getLevel(karma);
			if (calculatedLevel != level){
				publicInfo.setUnindexedProperty("level", calculatedLevel);
				datastore.put(publicInfo);
				level = calculatedLevel;
			}
		}

		req.setAttribute("username", username);
		req.setAttribute("karma", KarmaDescription.toMediumString(karma));
		req.setAttribute("profilePictureUrl", profilePictureUrl);
		req.setAttribute("profileUrl", profileUrl);
		req.setAttribute("email", email);
		
		req.setAttribute("userLevel", level);
		int upperThreshold = KarmaAPI.getKarmaThresholdForLevel(level + 1);
		int lowerThreshold = KarmaAPI.getKarmaThresholdForLevel(level);
		req.setAttribute("progressThroughLevel", 100 * (karma - lowerThreshold)/(upperThreshold-lowerThreshold));
	}
	
	private static void addUserNotificationsToRequest(HttpServletRequest req, User user){
		
		MenuItem[] notifications;
		if (user != null){
			notifications = new MenuItem[1];
			notifications[0] = new MenuItem("#", "", "Welcome to CalcU", "Click here to learn the ropes!", "Today", "", "", "", "/_static/img/avatar2.png");
			
		} else {
			notifications = new MenuItem[3];
			
			notifications[0] = new MenuItem("#", "", "A vibrant Community!", "Get and Give Karma for good deeds!", "4:45 PM", "", "", "", "/_static/img/avatar2.png");
			notifications[1] = new MenuItem("#", "", "Help each other out!", "Questions are answered quickly!", "Yesterday", "", "", "", "/_static/img/avatar3.png");
			notifications[2] = new MenuItem("#", "", "Get Feedback!", "Become a better student or teacher!", "12/15/14","", "", "", "/_static/img/avatar.png");
		}
		req.setAttribute("notificationsMenu", notifications);
	}
	
	private static void addUserRecommendationsToRequest(HttpServletRequest req, User user){
		
		MenuItem[] recommendations;
		
		if (user != null){
			recommendations = new MenuItem[4];
			recommendations[0] = new MenuItem("#", "", "Integration by Parts", "", "", "80%", "aqua", "", "");
			recommendations[1] = new MenuItem("#", "", "Fourier Series + Boundary Values", "", "", "30%", "yellow", "", "");
			recommendations[2] = new MenuItem("#", "", "Integration of Exponentials", "", "", "50%", "green", "", "");
			recommendations[3] = new MenuItem("#", "", "Taylor Series Polynomials", "", "", "20%", "red", "", "");
		} else {
			recommendations = new MenuItem[4];
			recommendations[0] = new MenuItem("#", "", "Using Big Data, we suggest", "", "", "80%", "aqua", "", "");
			recommendations[1] = new MenuItem("#", "", "Content for you to check out.", "", "", "30%", "yellow", "", "");
			recommendations[2] = new MenuItem("#", "", "We deduce your knowledge and needs", "", "", "50%", "green", "", "");
			recommendations[3] = new MenuItem("#", "", "And direct you to the best resources!", "", "", "20%", "red", "", "");
		}
		req.setAttribute("recommendationsMenu", recommendations);
	}

	public static void addPublicProfileInformationToRequest(HttpServletRequest req, String userId){
		Entity publicInfo = UserPublicInfoAPI.getOrCreateUserPublicInfo(userId);
		
		req.setAttribute("profileUsername", (String) publicInfo.getProperty("username"));
		int karma = ((Long) publicInfo.getProperty("karma")).intValue();
		int level = KarmaAPI.getLevel(karma);
		req.setAttribute("profileLevel", level);
		req.setAttribute("profileKarma", KarmaDescription.toLongString(karma));
		req.setAttribute("profileProfilePictureUrl", (String) publicInfo.getProperty("profilePictureUrl"));		
	
		int maxToDisplay = 10;
		List<Content> content = ContentAPI.getContentWithAuthor(userId, maxToDisplay, 1);
		List<MenuItem> userContent = new ArrayList<MenuItem>();
		for (Content c : content){
			userContent.add(new MenuItem(c));
		}
		req.setAttribute("profileUserContent", userContent);
		
		KarmaAPI.setKarmaProfileAttributes(req, userId);
		
		
	}

	public static void addPersonalProfileContextToRequest(HttpServletRequest req) {
		User user = userService.getCurrentUser();
		
		Entity privateInfo = UserPrivateInfoAPI.getOrCreateUserPrivateInfo(user);
		
		req.setAttribute("emailReply", (String) privateInfo.getProperty("emailReply"));
		req.setAttribute("emailRecommend", (String) privateInfo.getProperty("emailRecommend"));
		req.setAttribute("emailKarma", (String) privateInfo.getProperty("emailKarma"));
		
		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		UploadOptions uploadOptions = UploadOptions.Builder.withMaxUploadSizeBytes(5 * 1024L * 1024L);
		String uploadUrl = bs.createUploadUrl("/upload-profile-picture", uploadOptions);
		
		req.setAttribute("profilePictureUpload", uploadUrl);
	}
}
