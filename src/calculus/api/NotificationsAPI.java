package calculus.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;

import calculus.models.Notification;
import calculus.utilities.MenuItem;
import calculus.utilities.Settings;

public class NotificationsAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void sendNotification(Notification n){
		String recipient = n.getRecipientId();
		addUserNotification(recipient, n);
	}
	
	public static List<MenuItem> getUserNotifications(String userId){
		List<String> jsonNotifications = (List<String>) getNotificationsEntity(userId).getProperty("notifications");
		List<MenuItem> notifications = new ArrayList<MenuItem>();
		if (jsonNotifications == null) return new ArrayList<MenuItem>();
		for (String json : jsonNotifications){
			Notification n = Notification.fromJson(json);
			notifications.add(n.getMenuItem());
		}
		return notifications;	
	}
	
	private static void addUserNotification(String userId, Notification n){
		Entity userNotifications = getNotificationsEntity(userId);
		List<String> notificationJsons = (List<String>) userNotifications.getProperty("notifications");
		if (notificationJsons == null) notificationJsons = new ArrayList<String>();
		notificationJsons.add(n.toJson());
		userNotifications.setUnindexedProperty("notifications", notificationJsons);
		datastore.put(userNotifications);
	}
	
	public static void removeUserNotification(String userId, String notificationUuid){
		Entity userNotifications = getNotificationsEntity(userId);
		List<String> notificationJsons = (List<String>) userNotifications.getProperty("notifications");
		if (notificationJsons == null) return;
		for (int i = 0; i < notificationJsons.size(); i++){
			String json = notificationJsons.get(i);
			Notification n = Notification.fromJson(json);
			if (n.getUuid().equals(notificationUuid)){
				notificationJsons.remove(i);
			}
		}
		userNotifications.setUnindexedProperty("notifications", notificationJsons);
		datastore.put(userNotifications);
	}
	
	private static Entity getNotificationsEntity(String userId){
		Key key = KeyFactory.createKey("UserNotifications", userId);
		Entity notificationsEntity;
		try {
			notificationsEntity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			notificationsEntity = new Entity(key);
			notificationsEntity.setUnindexedProperty("userId", userId);
			ArrayList<String> notifications = new ArrayList<String>();
			Notification welcome = NotificationsAPI.welcomeNotification(userId);
			notifications.add(welcome.toJson());
			notificationsEntity.setUnindexedProperty("notifications", notifications);
			datastore.put(notificationsEntity);
		}
		return notificationsEntity;	
	}

	private static Notification welcomeNotification(String recipientId) {
		String title = "Welcome To CalcU!";
		String body = "Click here for an introduction to the Site!";
		String url = "/introduction";
		
		return new Notification()
			.withRecipientId(recipientId)
			.withTitle(title)
			.withTimeNow()
			.withBody(body)
			.withUrl(url)
			.withColor("info")
			.withAssociatedUserId(Settings.ADMIN_USER_ID);
	}

	public static void addUserNotificationsToRequest(HttpServletRequest req, User user) {
		if (user == null){
			MenuItem[] notifications = new MenuItem[3];
			notifications[0] = new MenuItem("#", "", "A vibrant Community!", "Get and Give Karma for good deeds!", "4:45 PM", "", "", "", "/_static/img/avatar2.png");
			notifications[1] = new MenuItem("#", "", "Help each other out!", "Questions are answered quickly!", "Yesterday", "", "", "", "/_static/img/avatar3.png");
			notifications[2] = new MenuItem("#", "", "Get Feedback!", "Become a better student or teacher!", "12/15/14","", "", "", "/_static/img/avatar.png");
			req.setAttribute("notificationsMenu", notifications);
		} else {
			String userId = user.getUserId();
			List<MenuItem> notifications = getUserNotifications(userId);
			req.setAttribute("notificationsMenu", notifications);
		}
	}
}
