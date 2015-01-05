package calculus.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.ReportAPI;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class ReportContentServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		String contentUuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		String reason = "none";
		if (req.getParameter("irrelevantContent") != null) reason = "irrelevantContent";
		if (req.getParameter("inaccurateContent") != null) reason = "inaccurateContent";
		if (req.getParameter("inappropriateContent") != null) reason = "inappropriateContent";
		
		ReportAPI.fileReport(user, contentUuid, reason);
		
		String redirectUrl = "/contribute/report-thank-you";
		
		resp.sendRedirect(redirectUrl);
	}
	
}
