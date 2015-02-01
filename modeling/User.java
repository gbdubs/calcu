import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;


public class User extends Thread {

	private int skill;
	private double standardDeviation;
	private double spread;
	public Entity entity;
	private String uuid;
	private List<Problem> problems;
	
	private static Map<String, User> userLookup = new HashMap<String, User>();
	
	private User(List<Problem> problems){
		this.skill = (int) Math.ceil(100 * Math.random());
		this.standardDeviation = Math.random() * 10;
		this.spread = 2 + Math.random() * 1;
		this.uuid = UUID.randomUUID().toString();
		this.problems = problems;
		
		this.entity = new Entity(KeyFactory.createKey("User", uuid));
		entity.setProperty("numRatings", 0);
		
		// Average ones are use to gauge reactions against
		entity.setProperty("averageDifficulty", 500);
		entity.setProperty("averageQuality", 500);
		entity.setProperty("averageHelpfulness", 500);
		
		// Perceived strength tells us how difficult they like their questions
		entity.setProperty("userStrength", 500);
		
		userLookup.put(uuid, this);
	}
	
	public void run(){
		while(true){
			int length = problems.size();
			int index = (int) Math.floor(Math.random() * length);
			Problem p = problems.get(index);
			int difficultyRating = howDifficultWasThisProblem(p);
			int qualityRating = howQualityWasThisProblem(p);
			int helpfulRating = 50;
			ModifiedRatingsAPI.submitRating(p.uuid, this.uuid, helpfulRating, difficultyRating, qualityRating);
			p.wasRatedAs(helpfulRating, difficultyRating, qualityRating);
			try {
				this.sleep((int) (Math.random() * 2000));
			} catch (InterruptedException e) {
			}
			
		}
	}
	
	private int howQualityWasThisProblem(Problem p) {
		int realQuality = p.realQuality;
		Random rand = new Random();
		double gaussian = rand.nextGaussian();
		double score = realQuality + 2 * gaussian * standardDeviation;
		return (int) Math.min(Math.max(score, 0), 100);
	}

	/**
	 * A Model of User Rating behavior.  Slightly random. Different spreads. Based on true skill.
	 * @param realDifficulty
	 * @return
	 */
	private int howDifficultWasThisProblem(Problem p){
		int realDifficulty = p.realDifficulty;
		double expectedValue = 40 + (realDifficulty - this.skill + (standardDeviation * (Math.random() - .5))) * spread;
		return (int) Math.min(Math.max(expectedValue, 0), 100);
	}
	
	public String toString(){
		return "UserWithSkill["+skill+"]spread["+spread+"]";
	}
	
	
	static Entity getOrCreateUserRatingProfile(String userId){
		User u = userLookup.get(userId);
		if (u == null) return null;
		return u.entity;
	}
}
