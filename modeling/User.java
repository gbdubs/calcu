import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;


public class User {

	private int skill;
	private double standardDeviation;
	private double spread;
	public Entity entity;
	private String uuid;
	
	private static Map<String, User> userLookup = new HashMap<String, User>();
	
	private User(){
		this.skill = (int) Math.ceil(100 * Math.random());
		this.standardDeviation = Math.random() * 10;
		this.spread = 2 + Math.random() * 1;
		this.uuid = UUID.randomUUID().toString();
		
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
