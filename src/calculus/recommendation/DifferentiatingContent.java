package calculus.recommendation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class DifferentiatingContent {
	private static Map<String, String> getContentDefinition(int i){
		Map<String, String> contentDefinition = new HashMap<String, String>();
		
		String char1 = "a";
		String title1 = "";
		String body1 = "";
		String author1 = "";
		String karma1 = "";
		String char2 = "A";
		String title2 = "";
		String body2 = "";
		String author2 = "";
		String karma2 = "";
		
		if (i == 0){
			
		} else if (i == 1) {
			
		} else if (i == 2) {
			
		} else if (i == 3) {
			
		} else if (i == 4) {
			
		} else if (i == 5) {
			
		} else if (i == 6) {
			
		} else if (i == 7) {
			
		} else if (i == 8) {
			
		} else if (i == 9) {
			
		} else {
			throw new RuntimeException("Higher Differentiating Content Beyond 10 Not supported.");
		}
		
		// To right-left randomize
		int rightLeftRandomization = (int) (Math.random() * 2);
		
		if (rightLeftRandomization == 0){
			contentDefinition.put("comparisonChar1", char1);
			contentDefinition.put("comparisonChar2", char2);
			contentDefinition.put("comparisonBody1", body1);
			contentDefinition.put("comparisonBody2", body2);
			contentDefinition.put("comparisonTitle1", title1);
			contentDefinition.put("comparisonTitle2", title2);
			contentDefinition.put("comparisonAuthor1", author1);
			contentDefinition.put("comparisonAuthor2", author2);
			contentDefinition.put("comparisonKarma1", karma1);
			contentDefinition.put("comparisonKarma2", karma2);
		} else {
			contentDefinition.put("comparisonChar1", char2);
			contentDefinition.put("comparisonChar2", char1);
			contentDefinition.put("comparisonBody1", body2);
			contentDefinition.put("comparisonBody2", body1);
			contentDefinition.put("comparisonTitle1", title2);
			contentDefinition.put("comparisonTitle2", title1);
			contentDefinition.put("comparisonAuthor1", author2);
			contentDefinition.put("comparisonAuthor2", author1);
			contentDefinition.put("comparisonKarma1", karma2);
			contentDefinition.put("comparisonKarma2", karma1);
		}
		
		return contentDefinition;
	}
	
	public static void placeContentDefinitionIntoRequest(HttpServletRequest req, int i){
		Map<String, String> map = getContentDefinition(i);
		for(String s : map.keySet()){
			req.setAttribute(s, map.get(s));
		}
	}
}
