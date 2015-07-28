package calculus.general;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentApprovalAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ContentApprovalServlet extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String uuid = req.getParameter("contentUuid");
		
		if (req.getParameter("approved") != null){
			ContentApprovalAPI.markContentApproved(uuid);
		} else if (req.getParameter("not-approved") != null){
			ContentApprovalAPI.markContentNotApproved(uuid);
		}
		
		resp.sendRedirect("/content-approval");
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if (req.getRequestURI().contains("data-please")){
			Entity e = ContentApprovalAPI.getContentApprovalEntity();
			PrintWriter pw = resp.getWriter();
			pw.println(e.toString());
			return;
		}
		
		Content c = ContentApprovalAPI.getPieceOfContentToApprove();
		UserContextAPI.addUserContextToRequest(req, "/content-approval");
		resp.setContentType("text/html");
		RequestDispatcher jsp;
		req.setAttribute("contentApprovalMode", true);
		
		if (c.getContentType().equals("question")){
			Question q = (Question) c;
			req.setAttribute("question", q);
			jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");
		} else if (c.getContentType().equals("practiceProblem")){
			PracticeProblem pp = (PracticeProblem) c;
			req.setAttribute("practiceProblem", pp);
			jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");
		} else if (c.getContentType().equals("textContent")){
			TextContent tc = (TextContent) c;
			req.setAttribute("textContent", tc);
			jsp = req.getRequestDispatcher("/WEB-INF/pages/content/text-content.jsp");
			
		} else {
			jsp = req.getRequestDispatcher("/WEB-INF/pages/page-not-found.jsp");
		}
		
		jsp.forward(req, resp);
	}
}