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
		
		if (user == null){
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			req.setAttribute("pageName", "Practice Problem Creation");
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/page-requires-login.jsp");
			jsp.forward(req, resp);
			return;
		}
		
		List<PracticeProblem> unsubmittedPP = PracticeProblemAPI.getUnsubmittedPracticeProblems(user);
		List<PracticeProblem> submittedPP = PracticeProblemAPI.getSubmittedPracticeProblems(user);
		
		System.out.println("Submitted PP = " + submittedPP.toString());
		System.out.println("Unubmitted PP = " + unsubmittedPP.toString());
		
		String urlRequest = req.getRequestURI();
		urlRequest = urlRequest.substring(urlRequest.indexOf("/practice-problem") +  17);
		
		if (urlRequest.startsWith("/new") || (unsubmittedPP.size() + submittedPP.size() == 0)){
			
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			
			resp.setContentType("text/html");
		
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
			jsp.forward(req, resp);

		} else if (urlRequest.startsWith("/edit/")){

			String uuid = urlRequest.substring(6);
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/edit/" + uuid);
			
			PracticeProblem pp = new PracticeProblem(uuid);
			PracticeProblemAPI.addPracticeProblemContext(req, pp);
			
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem.jsp");
			jsp.forward(req, resp);
		
		} else {
			
			UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/new");
			req.setAttribute("unsubmittedPracticeProblems", unsubmittedPP);
			req.setAttribute("submittedPracticeProblems", submittedPP);
			
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem-dashboard.jsp");
			jsp.forward(req, resp);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		PracticeProblemAPI.createOrUpdatePracticeProblemFromRequest(req);
		
		resp.setContentType("text/html");
		UserContextAPI.addUserContextToRequest(req, "/contribute/practice-problem/dashboard");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/practice-problem-thanks.jsp");
		jsp.forward(req, resp);
	}
}
