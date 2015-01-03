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

public class ViewContentServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, req.getRequestURI());
			
		String uuid = getUuidFromUrl(req.getRequestURI());
		
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
	
	public static String getUuidFromUrl(String url){
		
		String[] potentials = url.split("-|/");

		int[] expected = {8, 4, 4, 4, 12};
		int[] actual = new int[potentials.length];
		for (int i = 0; i < potentials.length; i++){ actual[i] = potentials[i].length(); }
		
		int endLocation = -1;
		for(int i = 0; i < actual.length; i++){
			int e = 0;
			int a = i;
			while (expected[e] == actual[a]){
				e++; a++;
				if (e == expected.length){
					endLocation = a;
					break;
				}
			}
		}
		if (endLocation == -1)
			return null;
		
		int stringEndLocation = -1;
		int i = 0;
		while (i < endLocation){
			stringEndLocation += potentials[i].length() + 1;
			i++;
		}
		
		int stringStartLocation = stringEndLocation - 36;
		
		return url.substring(stringStartLocation, stringEndLocation);
	}
}
