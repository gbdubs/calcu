package calculus.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.admin.RecalculateRecommendationsServlet;
import calculus.api.AchievementsAPI;
import calculus.api.KarmaAPI;
import calculus.api.TextContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.recommendation.DifferentiatingContent;
import calculus.recommendation.InterestsAPI;
import calculus.recommendation.PhenotypeAPI;
import calculus.recommendation.PublicRecommendationAPI;
import calculus.recommendation.SkillsAPI;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
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
		String userId = user.getUserId();
		
		if (stepNumber == -1){
			resp.sendRedirect("/personalize/landing");
			return;
		}
		
		UserContextAPI.addUserContextToRequest(req, "/personalize");
		req.setAttribute("stepNumber", stepNumber);
		resp.setContentType("text/html");
		if (stepNumber == 11){
			String[] properties = {"personalized"};
			AchievementsAPI.incrementUserStats(userId, properties);
			KarmaAPI.incrementUserKarmaForPersonalization(userId);
			Queue queue = QueueFactory.getQueue("calculateRecommendationsQueue");
			queue.addAsync(RecalculateRecommendationsServlet.updateRecommendationsForUser(userId));
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/complete.jsp");	
			jsp.forward(req, resp);
			return;
		} else if (stepNumber > 11 || stepNumber < 0){
			resp.sendRedirect("/personalize/landing");
		}
		if (stepNumber % 3 == 1) {
			// Interest Recognition
			Map<String, Boolean> interests = InterestsAPI.getPotentialAndExistingInterests(user.getUserId(), 24);
			req.setAttribute("interests", interests);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/interests.jsp");	
			jsp.forward(req, resp);
			return;
		} else if (stepNumber % 3 == 0 && stepNumber % 2 == 0) {
			// Difficulty Calibration Practice Problem
			PracticeProblem pp = PublicRecommendationAPI.getDifficultyCalibrationPracticeProblem(user.getUserId());
			req.setAttribute("difficultyCalibration", true);
			req.setAttribute("practiceProblem", pp);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/practice-problem.jsp");	
			jsp.forward(req, resp);
			return;
		} else if (stepNumber % 3 == 0 && stepNumber % 2 == 0) {
			// Difficulty Calibration Question
			Question q = PublicRecommendationAPI.getDifficultyCalibrationQuestion(user.getUserId());
			req.setAttribute("difficultyCalibration", true);
			req.setAttribute("question", q);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/content/question.jsp");	
			jsp.forward(req, resp);
			return;
		} else {
			// TextContent Comparison
			int questionToAsk = PhenotypeAPI.whatQuestionToAskUser(user.getUserId());
			if (questionToAsk == -1){
				resp.sendRedirect("/personalize/" + (stepNumber + 1));
				return;
			}
			DifferentiatingContent.placeContentDefinitionIntoRequest(req, questionToAsk);
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/personalize/comparison.jsp");
			jsp.forward(req, resp);
			return;
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String url = req.getRequestURI();
		
		if (url.contains("/personalize/interests")){
			String userId = req.getParameter("userId");
			String addInterests = req.getParameter("add-interests");
			String removeInterests = req.getParameter("remove-interests");
			
			String[] moreInterested = addInterests.split(",");
			String[] lessInterested = removeInterests.split(",");
			
			List<String> interestedTags = new ArrayList<String>();
			for(String s : moreInterested){
				if (s.length() > 0){
					interestedTags.add(s);
				}
			}
			List<String> disinterestedTags = new ArrayList<String>();
			for(String s : lessInterested){
				disinterestedTags.add(s);
			}
			InterestsAPI.userIndicatedTagsDisinterestingInCalibration(userId, disinterestedTags);
			InterestsAPI.userIndicatedTagsInterestingInCalibration(userId, interestedTags);
			
		} else if (url.contains("/personalize/difficulty")){
			String userId = req.getParameter("userId");
			String contentUuid = req.getParameter("contentUuid");
			String difficulty = req.getParameter("difficulty");
			float diff = Float.parseFloat(difficulty);
			SkillsAPI.contentDifficultyPersonalization(userId, contentUuid, diff);
		} else if (url.contains("/personalize/comparison")){
			String userId = req.getParameter("userId");
			String preference = req.getParameter("preferenceCharacter");
			char preferenceChar = preference.charAt(0);
			PhenotypeAPI.updateUserPhenotype(userId, preferenceChar);
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
