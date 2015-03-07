package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.PotentialContent;
import calculus.utilities.LoremIpsum;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PotentialContentAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static List<PotentialContent> getAllPotentialContent() {
		return getDummyPotentialContent();
		/*Query q = new Query("PotentialContent").addSort("createdAt", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<PotentialContent> content = new ArrayList<PotentialContent>();
		for(Entity e : pq.asIterable()){
			content.add(PotentialContent.fromEntity(e));
		}
		return content;*/
	}

	public static String createContentFromPotential(String uuid, String contentType){
		PotentialContent pc = PotentialContent.get(uuid);
		if (pc == null) return null;
		if (contentType.equals("practiceProblem")){
			return createPracticeProblemFromPotential(pc);
		} else if (contentType.equals("question")){
			return createQuestionFromPotential(pc);
		} else if (contentType.equals("textContent")){
			return createTextContentFromPotential(pc);
		} else {
			return null;
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
	
	private static List<PotentialContent> getDummyPotentialContent(){
		List<PotentialContent> result = new ArrayList<PotentialContent>();
		LoremIpsum random = new LoremIpsum();
		for(int i = 0; i < 10; i++){
			String title = random.getWords(5, Math.min(i * 5, 45));
			String body = random.getParagraphs(1);
			String solution = random.getParagraphs((int)(2 *Math.random()));
			String tags = random.getWords((int) (Math.random() * 10));
			String source = "http://lorempispum.com";
			result.add(new PotentialContent().withBody(body).withSolution(solution).withTitle(title).withTags(tags).withSource(source));
		}
		return result;
	}
}
