package calculus.general;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.PracticeProblemAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.utilities.UuidTools;

public class ViewContentServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
			
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		
		if (uuid == null){
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
			jsp.forward(req, resp);
			return;
		} 
			
		String contentType = Content.getContentType(uuid);
		
		if (contentType.equals("practiceProblem")){
			PracticeProblem pp = new PracticeProblem(uuid);
			if (pp.getSubmitted() && pp.getViewable()){
				PracticeProblemAPI.addPracticeProblemContext(req, pp);
				resp.setContentType("text/html");
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem-part-two.jsp");
				jsp.forward(req, resp);
				
			} else {
				RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
				jsp.forward(req, resp);
			}
		} else {
			resp.getWriter().println("There is an issue. An unsupported content type <b>'"+ contentType +"'</b> was requested to be displayed.");
		}
	}
	
	
}
