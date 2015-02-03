package calculus.general;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.ReportAPI;
import calculus.api.UserContextAPI;
import calculus.utilities.UuidTools;

@SuppressWarnings("serial")
public class ReportContentServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String contentUuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		String reason = "none";
		if (req.getParameter("irrelevantContent") != null) reason = "irrelevantContent";
		if (req.getParameter("inaccurateContent") != null) reason = "inaccurateContent";
		if (req.getParameter("inappropriateContent") != null) reason = "inappropriateContent";
		if (req.getParameter("proprietaryContent") != null) reason = "proprietaryContent";
		
		if (user == null) return;
		
		ReportAPI.fileReport(user, contentUuid, reason);
		
		req.setAttribute("readableContentType", "a report");
		UserContextAPI.addUserContextToRequest(req, "/home");
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/content-thanks.jsp");	
		jsp.forward(req, resp);
	}
	
}
