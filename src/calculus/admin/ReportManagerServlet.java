package calculus.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import calculus.api.ReportAPI;

@SuppressWarnings("serial")
public class ReportManagerServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		// boolean admin = UserServiceFactory.getUserService().isUserAdmin();
		boolean admin = true;
		
		if (admin){
			String reportUuid = req.getParameter("reportUuid");
			String action = req.getParameter("action");
			
			if (action.equals("delete-content")){
				ReportAPI.deleteContent(reportUuid);
			} else if (action.equals("delete-report")){
				ReportAPI.deleteReport(reportUuid);
			} else if (action.equals("flag-reporter")){
				ReportAPI.flagReporterForAbuse(reportUuid);
			}
		} else {
			return;
		}
	}
	
}
