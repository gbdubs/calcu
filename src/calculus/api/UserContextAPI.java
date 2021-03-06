package calculus.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Content;
import calculus.recommendation.PublicRecommendationAPI;
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
		
		req.setAttribute("isAdmin", user != null && userService.isUserAdmin());
		req.setAttribute("user", user);
		req.setAttribute("loginUrl", loginUrl);
		req.setAttribute("logoutUrl", logoutUrl);

		addUserPublicInfoToRequest(req, user);
		BookmarksAPI.addUserBookmarksToRequest(req, user);
		NotificationsAPI.addUserNotificationsToRequest(req, user);
		PublicRecommendationAPI.addUserRecommendationsToRequest(req, user);
	}
	
	private static void addUserPublicInfoToRequest(HttpServletRequest req, User user){
		
		String username = "Anonymous Elephant";
		int karma = 0; 
		int level = 1;
		String profilePictureUrl = "/_static/img/elephant.png";
		String profileUrl = "/";
		String email = "anonymous314159@gmail.com";
		boolean mustConsent = false;
		
		if (user != null){
			Entity publicInfo = UserPublicInfoAPI.getOrCreateMyPublicInfo(user);
			karma = ((Long) publicInfo.getProperty("karma")).intValue();
			level = ((Long) publicInfo.getProperty("level")).intValue();
			username = (String) publicInfo.getProperty("username");
			profilePictureUrl = (String) publicInfo.getProperty("profilePictureUrl");
			profileUrl = (String) publicInfo.getProperty("profileUrl");
			email = (String) publicInfo.getProperty("email");
			if (publicInfo.getProperty("affirmedConsent") == null){
				mustConsent = true;
			}
		}

		req.setAttribute("mustConsent", mustConsent);
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
		long l = 0;
		for (Content c : content){
			l = Math.max(c.getCreatedAt(), l);
			userContent.add(new MenuItem(c));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  MM-DD-YY");
		
		req.setAttribute("profileUserContent", userContent);
		
		req.setAttribute("profileLastSubmission", sdf.format(l));
		
		String numContributions = "" + userContent.size();
		if (userContent.size() == maxToDisplay) numContributions += "+";
		req.setAttribute("profileNumberContributions", numContributions);
		
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
		
		int maxToDisplay = 10;
		List<Content> content = ContentAPI.getContentWithAuthor(user.getUserId(), maxToDisplay, 1);
		List<MenuItem> userContent = new ArrayList<MenuItem>();
		long l = 0;
		for (Content c : content){
			l = Math.max(c.getCreatedAt(), l);
			userContent.add(new MenuItem(c));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  MM-DD-YY");
		
		req.setAttribute("profileUserContent", userContent);
		
		req.setAttribute("profileLastSubmission", sdf.format(l));
		
		req.setAttribute("profilePictureUpload", uploadUrl);
		
		KarmaAPI.setKarmaProfileAttributes(req, user.getUserId());
	}
}
