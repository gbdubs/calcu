package calculus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import calculus.models.Content;
import calculus.models.Report;

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
		
		datastore.put(entity);
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
		datastore.delete(reportKey);
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
				Content content = new Content((String) result.getProperty("contentUuid"));
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
			datastore.delete(KeyFactory.createKey("Content", contentUuid));
		} catch (EntityNotFoundException e) {
			// No need to delete it if it does not exist!
		}
		datastore.delete(reportKey);
	}

	public static void flagReporterForAbuse(String reportUuid) {
		// Send a message to the User, Scolding them
		// Send a message to the reporter, Thanking them
	}
	
}
