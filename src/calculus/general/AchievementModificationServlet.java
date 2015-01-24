package calculus.general;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.AchievmentsAPI;
import calculus.api.UserContextAPI;

@SuppressWarnings("serial")
public class AchievementModificationServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String action = req.getParameter("action");
		
		if (action.equals("delete")){
			AchievmentsAPI.deleteAchievement(req.getParameter("uuid"));
		} else if (action.equals("create")){
			
			String name = req.getParameter("name");
			String description = req.getParameter("description");
			String qualification = req.getParameter("qualification");
			String icon = req.getParameter("icon");
			String color = req.getParameter("color");
			String secondaryColor = req.getParameter("secondaryColor");
			
			AchievmentsAPI.createNewAchievment(name, icon, color, secondaryColor, description, qualification);
		
			resp.sendRedirect("/admin");
		}
	}
}
