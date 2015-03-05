package calculus.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.api.TagAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;
import calculus.recommendation.InterestsAPI;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SearchByTagServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String requestUri = req.getRequestURI();
		UserContextAPI.addUserContextToRequest(req, requestUri);
		
		String tagString = getTagStringFromUrlPattern(requestUri);
		
		if (tagString == null){
			resp.setContentType("text/html");
			RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
			jsp.forward(req, resp);
		} else {
			renderSearchResultsForString(tagString, req, resp);
			return;
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String tagString = req.getParameter("tagsInput");
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user != null) {
			InterestsAPI.userSearchedForTags(user.getUserId(), TagAPI.getTagsFromString(tagString));
		}
		resp.sendRedirect("/search/" + tagString);
	}
	
	private String getTagStringFromUrlPattern(String requestUri) {
		int index = requestUri.indexOf("/search/") + 8;
		int endingIndex = requestUri.length();
		if (requestUri.contains("?")) endingIndex = requestUri.indexOf("?");
		if (index < 8) return null;
		return requestUri.substring(index, endingIndex).replaceAll("%20", " ");
	}
	
	private int getSeedNumber(HttpServletRequest req){
		Object seedNumber = req.getParameter("seed");
		if (seedNumber == null) return 1;
		return Integer.parseInt((String) seedNumber);
	}

	private void renderSearchResultsForString(String tagString, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<Content> practiceProblems = new ArrayList<Content>();
		List<Content> questions = new ArrayList<Content>();
		List<Content> textContent = new ArrayList<Content>();
		boolean moreResults = false;
		Set<String> recommendedTags = null;
		int seed = 1;
		
		if(!tagString.trim().equals("") && tagString != null){
			String[] tags = tagString.trim().split(",");
			
			int maxNumResults = 50;
			seed = getSeedNumber(req);
			List<String> uuids = TagAPI.getUuidsResultsOfMultipleTags(tags, maxNumResults, seed);
			List<Content> content = ContentAPI.getContentAsync(uuids);
			for (Content c : content){
				String contentType = c.getContentType();
				if (contentType.equals("practiceProblem")){
					practiceProblems.add(c);
				} else if (contentType.equals("question")){
					questions.add(c);
				} else if (contentType.equals("textContent")){
					textContent.add(c);
				}
			}
			
			if (uuids.size() == maxNumResults){
				moreResults = true;
			}
			
			if (practiceProblems.size() < 10 && questions.size() < 10 && textContent.size() < 10) {
				recommendedTags = new HashSet<String>();
				for (String tag : tags){
					recommendedTags.addAll(TagAPI.getSimilarTags(tag));
				}
				recommendedTags.removeAll(Arrays.asList(tags));
			}
		}
		
		req.setAttribute("seed", seed);
		req.setAttribute("moreResults", moreResults);
		req.setAttribute("tags", tagString);
		req.setAttribute("recommendedTags", recommendedTags);
		req.setAttribute("resultPracticeProblems", practiceProblems);
		req.setAttribute("resultQuestions", questions);
		req.setAttribute("resultTextContent", textContent);
		
		if (practiceProblems.size() == 0){
			req.setAttribute("practiceProblemsNotFound", true);
		} else {
			req.setAttribute("practiceProblemsNotFound", false);
		}
		
		if (questions.size() == 0){
			req.setAttribute("questionsNotFound", true);
		} else {
			req.setAttribute("questionsNotFound", false);
		}
		
		if (textContent.size() == 0){
			req.setAttribute("textContentNotFound", true);
		} else {
			req.setAttribute("textContentNotFound", false);
		}
		
		UserContextAPI.addUserContextToRequest(req, "/search/" + tagString);
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/search.jsp");	
		
		jsp.forward(req, resp);
	}
	
}
