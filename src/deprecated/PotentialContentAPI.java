package deprecated;

import java.util.ArrayList;
import java.util.List;

import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.TextContentAPI;
import calculus.utilities.LoremIpsum;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PotentialContentAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<PotentialContent> getAllPotentialContent(int maxResults) {
		Query q = new Query("PotentialContent").addSort("createdAt", SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<PotentialContent> content = new ArrayList<PotentialContent>();
		for(Entity e : pq.asIterable(FetchOptions.Builder.withLimit(40))){
			content.add(PotentialContent.fromEntity(e));
		}
		return content;
	}

	public static String createContentFromPotential(String uuid, String contentType){
		PotentialContent pc = PotentialContent.get(uuid);
		if (pc == null) return null;
	    String newUuid = null;
		if (contentType.equals("practiceProblem")){
			newUuid = createPracticeProblemFromPotential(pc);
		} else if (contentType.equals("question")){
			newUuid = createQuestionFromPotential(pc);
		} else if (contentType.equals("textContent")){
			newUuid = createTextContentFromPotential(pc);
		} 
		if (newUuid == null) {
			return null;
		} else {
			deletePotentialContent(uuid);
			return newUuid;
		}
	}

	public static void deletePotentialContent(String uuid){
		Key key = KeyFactory.createKey("PotentialContent", uuid);
		datastore.delete(key);
	}
	
	private static String createPracticeProblemFromPotential(PotentialContent pc){
		String title = pc.getTitle();
		String body = pc.getBody();
		String solution = pc.getSolution();
		String tags = pc.getTagsAsString();
		String source = pc.getSource();
		String newUuid = PracticeProblemAPI.createNewPracticeProblemFromUpload(title, body, solution, tags, source);
		return newUuid;
	}
	
	
	private static String createQuestionFromPotential(PotentialContent pc){
		String title = pc.getTitle();
		String body = pc.getBody();
		String tags = pc.getTagsAsString();
		String source = pc.getSource();
		String newUuid = QuestionAPI.createNewQuestionFromUpload(title, body, tags, source);
		return newUuid;
	}
	
	private static String createTextContentFromPotential(PotentialContent pc){
		String title = pc.getTitle();
		String body = pc.getBody();
		String tags = pc.getTagsAsString();
		String source = pc.getSource();
		String newUuid = TextContentAPI.createNewTextContentFromUpload(title, body, tags, source);
		return newUuid;
	}

	public static void updatePotentialContent(String uuid, String title, String body, String solution, String tags) {
		PotentialContent pc = PotentialContent.get(uuid);
		if (pc == null) return;
		pc.withBody(body).withTitle(title).withSolution(solution).withTags(tags);
		pc.save();
	}
}
