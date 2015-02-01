import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Problem {

	public int realDifficulty;
	public Entity entity;
	public String uuid;
	public int realQuality;
	private String hHist;
	private String dHist;
	private String qHist;
	private String rHist;
	public List<Integer> rError;
	
	public static Map<String, Problem> problemLookup = new HashMap<String, Problem>();
	
	public Problem(){
		realDifficulty = (int) (100.0 * Math.random());
		realQuality = (int) (100 * Math.random());
		
		rError = new ArrayList<Integer>();
		uuid = UUID.randomUUID().toString();
		entity = new Entity(uuid);
		entity.setProperty("numRatings", 0);
		
		dHist = "Acutal=["+realDifficulty+"] History=[500]";
		hHist = "[500]";
		qHist = "[500]";
		rHist = "[500]";
		
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
		long difficultyRating = entity.getProperty("difficultyRating");
		int error = (int) (difficultyRating - (10 * this.realDifficulty));
	//	System.out.println("difficultyRating:"+ entity.getProperty("difficultyRating") + "rError:" + error);
		rError.add(error);
	}
	
	public String toString(){
		return dHist;
	}
}
