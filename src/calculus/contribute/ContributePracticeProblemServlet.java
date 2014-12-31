package calculus.contribute;

import java.util.List;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import utilities.PracticeProblem;
import utilities.PracticeProblemAPI;
import utilities.UserContextAPI;

public class ContributePracticeProblemServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		List<PracticeProblem> unsubmittedPP = PracticeProblemAPI.getUnsubmittedPracticeProblems(user);
		List<PracticeProblem> submittedPP = PracticeProblemAPI.getSubmittedPracticeProblems(user);
		System.out.println(unsubmittedPP.toString());
		
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/practice-problem") +  17);
		//|| (unsubmittedPP.size() + submittedPP.size() == 0)
		if (urlRequest.startsWith("/new") ){
			
		
			resp.setContentType("text/html");
		
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
			jsp.forward(req, resp);

		} else if (urlRequest.startsWith("/edit/")){
			String uuid = urlRequest.substring(6);
			resp.getWriter().println("<p> IT STARTED WITH <b>" + urlRequest + "</b> Parsed UUID = (" + uuid + ") </p>");

		} else {
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			
			req.setAttribute("unsubmittedPracticeProblems", unsubmittedPP);
			
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem-dashboard.jsp");
			jsp.forward(req, resp);

		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		PracticeProblemAPI.storeNewPracticeProblemFromRequest(req);
		
		resp.setContentType("text/html");
		UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/dashboard");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem-thanks.jsp");
		jsp.forward(req, resp);
	}
}
