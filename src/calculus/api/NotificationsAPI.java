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
		// Goes in reverse order so that the oldest notifications appear on the bottom
		for (int i = jsonNotifications.size() - 1; i >= 0; i--){
			String json = jsonNotifications.get(i);
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
		String url = "/tutorial";
		
		return new Notification()
			.withRecipientId(recipientId)
			.withTitle(title)
			.withTimeNow()
			.withBody(body)
			.withUrl(url)
			.withColor("info")
			.withImageUrl("/_static/img/DerivativeAdmin.png")
			.withAssociatedUserId(Settings.ADMIN_USER_ID);
	}

	public static void addUserNotificationsToRequest(HttpServletRequest req, User user) {
		if (user == null){
			List<MenuItem> notifications = getLoggedOutNotifications();
			req.setAttribute("notificationsMenu", notifications);
		} else {
			String userId = user.getUserId();
			List<MenuItem> notifications = getUserNotifications(userId);
			req.setAttribute("notificationsMenu", notifications);
		}
	}
	
	private static List<MenuItem> getLoggedOutNotifications() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		menuItems.add(new MenuItem()
			.withTitle("A vibrant Community!")
			.withDescription("Get and Give Karma for good deeds!")
			.withTime("4:45PM")
			.withImage("/_static/img/avatar2.png")
		);
		menuItems.add(new MenuItem()
			.withTitle("Help each other out!")
			.withDescription("Questions are answered quickly!")
			.withTime("Yesterday")
			.withImage("/_static/img/avatar3.png")
		);
		menuItems.add(new MenuItem()
			.withTitle("Get Feedback!")
			.withDescription("Become a better student or teacher!")
			.withTime("2/15/15")
			.withImage("/_static/img/avatar.png")
		);
		return menuItems;
	}
}
