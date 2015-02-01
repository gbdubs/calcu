
public class User {

	private int skill;
	private double standardDeviation;
	private double spread;
	
	private String id;
	
	private User(){
		this.skill = (int) Math.ceil(100 * Math.random());
		this.standardDeviation = Math.random() * 10;
		this.spread = 2 + Math.random() * 1;
	}
	
	/**
	 * A Model of User Rating behavior.  Slightly random. Different spreads. Based on true skill.
	 * @param realDifficulty
	 * @return
	 */
	private int howDifficultWasThisProblem(int realDifficulty){
		double expectedValue = 40 + (realDifficulty - this.skill + (standardDeviation * (Math.random() - .5))) * spread;
		return (int) Math.min(Math.max(expectedValue, 0), 100);
	}
	
	public static void main(String[] args){
		User u1 = new User();
		for (int i = 0; i < 100; i++){
			System.out.println(u1.toString() + " rated problem that was " + i + " hard " + u1.howDifficultWasThisProblem(i));
		}
	}
	
	
	public String toString(){
		return "UserWithSkill["+skill+"]spread["+spread+"]";
	}
}
