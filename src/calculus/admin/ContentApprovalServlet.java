package calculus.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PotentialContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.PotentialContent;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContentApprovalServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserService us = UserServiceFactory.getUserService();
		
		if (us.isUserLoggedIn() && us.isUserAdmin()){
			List<PotentialContent> potentialContent = PotentialContentAPI.getAllPotentialContent();
			req.setAttribute("potentialContent", potentialContent);
			
			if (!req.getRequestURI().equals("/admin-safe")){
				UserContextAPI.addUserContextToRequest(req, "/admin/content-approval");
			}
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/admin-content-approval.jsp");	
			jsp.forward(req, resp);
		} else {
			resp.sendRedirect("/page-not-found");
		}		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String uuid = req.getParameter("uuid");
		String action = req.getParameter("action");
		if (action.equals("create")){
			String contentType = req.getParameter("contentType");
			PotentialContentAPI.createContentFromPotential(uuid, contentType);
		} else if (action.equals("delete")){
			PotentialContentAPI.deletePotentialContent(uuid);
		} else if (action.equals("save")){
			String title = req.getParameter("title");
			String body = req.getParameter("body");
			String solution = req.getParameter("solution");
			String tags = req.getParameter("tags");
			
			PotentialContentAPI.updatePotentialContent(uuid, title, body, solution, tags);
		}
	}
}
