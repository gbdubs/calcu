package calculus.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.NotificationsAPI;
import calculus.models.Notification;
import calculus.utilities.Settings;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class UniversalNotificationServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String title = req.getParameter("title");
		String body = req.getParameter("body");
		String associatedUserId = Settings.ADMIN_USER_ID;
		String url = req.getParameter("url");
		long time = System.currentTimeMillis();
		String imageUrl = "/_static/img/IntegralAdmin.png";
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		Query q = new Query("UserNotifications");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			String userId = (String) e.getProperty("userId");
			Notification n = new Notification()
					.withRecipientId(userId)
					.withBody(body)
					.withTitle(title)
					.withImageUrl(imageUrl)
					.withAssociatedUserId(associatedUserId)
					.withTime(time)
					.withUrl(url)
					.withColor("info");
			NotificationsAPI.sendNotification(n);
		}
		
		resp.sendRedirect("/admin");
	}
}
