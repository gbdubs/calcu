package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import calculus.models.Content;
import calculus.models.Notification;
import calculus.models.Report;
import calculus.utilities.SafeList;
import calculus.utilities.Settings;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;

public class ReportAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	
	public static void fileReport(User user, String contentUuid, String reason){
		
		ContentAPI.setInvisible(contentUuid);
		
		long time = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString();
		
		Entity entity = new Entity(KeyFactory.createKey("ReportedContent", uuid));
		
		entity.setProperty("reporterUserId", user.getUserId());
		entity.setProperty("contentUuid", contentUuid);
		entity.setProperty("reason", reason);
		entity.setProperty("reportUuid", uuid);
		entity.setProperty("reportedAt", time);
		
		asyncDatastore.put(entity);
	}
	
	public static void deleteReport(String reportUuid) {
		Key reportKey = KeyFactory.createKey("ReportedContent", reportUuid);
		try {
			Entity report = datastore.get(reportKey);
			String contentUuid = (String) report.getProperty("contentUuid");
			ContentAPI.setVisible(contentUuid);
		} catch (EntityNotFoundException e) {
			// If it doesn't exist, we don't need to set it visible.
		}
		asyncDatastore.delete(reportKey);
	}

	public static List<Report> getAllOpenReports() {
		List<Report> list = new ArrayList<Report>();
		Query query = new Query("ReportedContent").addSort("reportedAt");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			String reportUuid = (String) result.getProperty("reportUuid");
			long reportedAt = (long) result.getProperty("reportedAt");
			String reportReason = (String) result.getProperty("reason");
			try {
				Content content = ContentAPI.instantiateContent((String) result.getProperty("contentUuid"));
				Entity userPublicInfo = UserPublicInfoAPI.getOrCreateUserPublicInfo((String) result.getProperty("reporterUserId"));
				Report report = new Report(reportUuid, reportedAt, reportReason, userPublicInfo, content);
				list.add(report);
			} catch (EntityNotFoundException e) {
				// If the content is not found, we simply don't include it in the list of reported content.
			}
		}
		return list;	
	}

	public static void deleteContent(String reportUuid) {
		// Send User Notification that their report was successful
		// Send content creator a notification that their content was taken down
		
		Key reportKey = KeyFactory.createKey("ReportedContent", reportUuid);
		try {
			Entity report = datastore.get(reportKey);
			String contentUuid = (String) report.getProperty("contentUuid");
			
			// Deletes all Children of the content.
			Entity content = datastore.get(KeyFactory.createKey("Content", contentUuid));
			List<String> children = SafeList.string(content, "allAnswers");

			for(String child : children){
				datastore.delete(KeyFactory.createKey("Content", child));
			}
			
			// Deletes the Parental reference.
			String parentUuid = (String) content.getProperty("parentUuid");
			if (parentUuid != null){
				Content parentContent = ContentAPI.instantiateContent(parentUuid);
				parentContent.removeAnswer(contentUuid);
			}
			
			// Reports the deletion to the user
			String reporterUserId = (String) report.getProperty("reporterUserId");
			sendSuccessfulReportNotifications(reporterUserId, contentUuid);
			
			// Deletes offending content.
			asyncDatastore.delete(KeyFactory.createKey("Content", contentUuid));
		} catch (EntityNotFoundException e) {
			// No need to delete it if it does not exist!
		}
		asyncDatastore.delete(reportKey);
	}

	private static void sendSuccessfulReportNotifications(String reporterUserId, String contentUuid) {
		
		String authorUserId = ContentAPI.getContentAuthorId(contentUuid);
		String authorUsername = UserPublicInfoAPI.getUsername(authorUserId);
		
		// SENDS THE REPORTER THE NOTIFICATION
		String title = "Thank You For Your Report!";
		String body = "Your report of " + authorUsername + "'s Content led to the content being taken down.";
		
		Notification toSend = new Notification()
			.withRecipientId(reporterUserId)
			.withTimeNow()
			.withTitle(title)
			.withBody(body)
			.withUrl("/user/" + authorUserId)
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withImageUrl("/_static/img/IntegralAdmin.png")
			.withColor("success");
		
		// SENDS THE AUTHOR THE NOTIFICATION 
		String title2 = "Your Content Was Taken Down";
		String body2 = "A piece of your content was taken down. If this happens again, you may be banned from the site. Please contact us if you think this was in error.";		
		
		Notification toSend2 = new Notification()
			.withRecipientId(authorUserId)
			.withTimeNow()
			.withColor("danger")
			.withImageUrl("/_static/img/IntegralAdmin.png")
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withUrl("/user/" + authorUserId)
			.withTitle(title2)
			.withBody(body2);
		
		NotificationsAPI.sendNotification(toSend);
		NotificationsAPI.sendNotification(toSend2);
		
		// GIVES THE REPORTER AN ACHIEVEMENT FOR THEIR REPORT
		AchievementsAPI.reporterAchievement(reporterUserId);
		
	}

	public static void flagReporterForAbuse(String reportUuid) {
		
		Key reportKey = KeyFactory.createKey("ReportedContent", reportUuid);
		Entity report;
		try {
			report = datastore.get(reportKey);
		} catch (EntityNotFoundException enfe){
			return;
		}
		
		String contentUuid = (String) report.getProperty("contentUuid");
		String reporterUserId = (String) report.getProperty("reporterUserId");
		String authorUserId = ContentAPI.getContentAuthorId(contentUuid);
		String authorUsername = UserPublicInfoAPI.getUsername(authorUserId);
		
		String title = "Be More Careful in What You Report!";
		String body = "Your report of " + authorUsername + "'s Content was found to not need removal. If you think this was in error, please contact us.";
		
		Notification toSend = new Notification()
			.withRecipientId(reporterUserId)
			.withTimeNow()
			.withTitle(title)
			.withBody(body)
			.withUrl("/content/" + contentUuid)
			.withAssociatedUserId(Settings.ADMIN_USER_ID)
			.withImageUrl("/_static/img/IntegralAdmin.png")
			.withColor("danger");
		
		// Send a message to the User, Scolding them
		NotificationsAPI.sendNotification(toSend);
		
		// Deletes the report, and sets the content visible again.
		deleteReport(reportUuid);
	}
}
