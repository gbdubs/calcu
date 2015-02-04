package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.NotificationsAPI;

@SuppressWarnings("serial")
public class NotificationServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String userId = req.getParameter("userId");
		String notificationUuid = req.getParameter("uuid");
		
		NotificationsAPI.removeUserNotification(userId, notificationUuid);
	}
}
