import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;


public class Problem {

	public int realDifficulty;
	public Entity entity;
	public String uuid;
	public int realQuality;
	private String hHist;
	private String dHist;
	private String qHist;
	private String rHist;
	
	public static Map<String, Problem> problemLookup = new HashMap<String, Problem>();
	
	public Problem(){
		realDifficulty = (int) (100.0 * Math.random());
		realQuality = (int) (100 * Math.random());
		
		uuid = UUID.randomUUID().toString();
		entity = new Entity(KeyFactory.createKey("Problem", uuid));
		entity.setProperty("numRatings", 0);
		
		// Average ones are use to gauge reactions against
		entity.setProperty("averageDifficulty", 500);
		entity.setProperty("averageQuality", 500);
		entity.setProperty("averageHelpfulness", 500);
		
		// Perceived strength tells us how difficult they like their questions
		entity.setProperty("difficultyRating", 500);
		
		problemLookup.put(uuid, this);
	}

	public static Entity getOrCreateContentRatingProfile(String contentUuid) {
		Problem p = problemLookup.get(contentUuid);
		if (p == null) return null;
		return p.entity;
	}

	public void wasRatedAs(int h, int d, int q) {
		hHist += ", "+h+"["+entity.getProperty("averageHelpfulness")+"]";
		dHist += ", "+d+"["+entity.getProperty("averageDifficulty")+"]";
		qHist += ", "+q+"["+entity.getProperty("averageQuality")+"]";
		rHist += ", ["+entity.getProperty("difficultyRating")+"]";
	}
	
	public String toString(){
		return rHist;
	}
}
