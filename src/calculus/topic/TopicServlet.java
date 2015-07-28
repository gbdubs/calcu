package calculus.topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.api.UserContextAPI;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.EntityNotFoundException;

@SuppressWarnings("serial")
public class TopicServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/topic");
		
		String uuid = UuidTools.getUuidFromUrl(req.getRequestURI());
		if (uuid != null){
			try {
				Topic t = new Topic(uuid);
				
				List<TextContent> tc = new ArrayList<TextContent>();
				List<PracticeProblem> pp = new ArrayList<PracticeProblem>();
				List<Question> qs = new ArrayList<Question>();
				
				for (String contentUuid : t.getContentUuids()){
					Content c = ContentAPI.instantiateContent(contentUuid);
					if (c.getContentType().equals("textContent")){
						tc.add((TextContent) c);
					} else if (c.getContentType().equals("question")){
						qs.add((Question) c);
					} else if (c.getContentType().equals("practiceProblem")){
						pp.add((PracticeProblem) c);
					}
				}
				
				req.setAttribute("topic", t);
				req.setAttribute("textContent", tc);
				req.setAttribute("practiceProblems", pp);
				req.setAttribute("questions", qs);
				req.setAttribute("familyTopics", t.getFamilyTopics());
				
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/topic.jsp");
		jsp.forward(req, resp);
	}
}
