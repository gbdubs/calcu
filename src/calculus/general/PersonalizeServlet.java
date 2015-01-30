package calculus.general;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.BookmarksAPI;
import calculus.api.RecommendationsAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class PersonalizeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String uri = req.getRequestURI();
		
		if (uri.equals("/personalize/landing") || user == null){
			UserContextAPI.addUserContextToRequest(req, "/personalize/landing");
			req.setAttribute("loggedIn", user != null);
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/landing.jsp");	
			jsp.forward(req, resp);
			return;
		}
		
		int stepNumber = getStepNumber(uri);
		
		if (stepNumber == -1){
			resp.sendRedirect("/personalize/landing");
			return;
		}
		
		UserContextAPI.addUserContextToRequest(req, "/personalize");
		req.setAttribute("stepNumber", stepNumber);
		resp.setContentType("text/html");
		if (stepNumber % 4 == 1) {
			// Interest Recognition
			Map<String, Boolean> interests = RecommendationsAPI.getUserInterestPossibilities(user.getUserId());
			req.setAttribute("interests", interests);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/interests.jsp");	
			jsp.forward(req, resp);
			return;
		} else if (stepNumber % 3 == 0 && stepNumber != 12) {
			// Difficulty Calibration Practice Problem
			PracticeProblem pp = RecommendationsAPI.getDifficultyCalibrationPracticeProblem(user.getUserId());
			req.setAttribute("difficultyCalibration", true);
			req.setAttribute("practiceProblem", pp);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");	
			jsp.forward(req, resp);
			return;
		} else if (stepNumber == 12) {
			// Difficulty Calibration Question
			Question q = RecommendationsAPI.getDifficultyCalibrationQuestion(user.getUserId());
			req.setAttribute("difficultyCalibration", true);
			req.setAttribute("question", q);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");	
			jsp.forward(req, resp);
			return;
		} else {
			
		}
		
		
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String url = req.getRequestURI();
		
		if (url.contains("/personalize/interest")){
			String userId = req.getParameter("userId");
			String interest = req.getParameter("interest");
			String action = req.getParameter("action");
			
			if (action.equals("add")){
				RecommendationsAPI.addInterest(interest, userId);
			} else if (action.equals("remove")){
				RecommendationsAPI.removeInterest(interest, userId);
			}
		} else if (url.contains("/personalize/difficulty")){
			String userId = req.getParameter("userId");
			String contentUuid = req.getParameter("contentUuid");
			String difficulty = req.getParameter("difficulty");
			
			RecommendationsAPI.addDifficultyInformation(userId, contentUuid, difficulty);
		}
	}
	
	private static int getStepNumber(String uri){
		int location = uri.indexOf("/personalize/");
		if (location == -1) return -1;
		
		String remainder = uri.substring(location + 13);
		try{
			return Integer.parseInt(remainder);
		} catch (NumberFormatException e){
			return -1;
		}
	}
	
}
