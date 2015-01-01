package calculus.utilities;

public class KarmaDescription {
	 
	/**
	 * Constructs a description of Karma that is 20 characters or fewer.
	 * @param k The User's Karma
	 * @return A Description of l<=20
	 */
	public static String toLongString(int k){
		return toMediumString(k);
	}

	/**
	 * Constructs a description of Karma that is 10 characters or fewer.
	 * @param k The User's Karma
	 * @return A Description of l<=10
	 */
	public static String toMediumString(int k){
		return toShortString(k) + " Karma";
	}
	
	/**
	 * Constructs a description of Karma that is 5 characters or fewer.
	 * @param k The User's Karma
	 * @return A Description of l<=5
	 */
	public static String toShortString(int k){
		if (k < 0) return "0";
		if (k < 1000) return "" + k;
		if (k < 10000) return (k / 10)/100.0 + " K";
		if (k < 100000) return (k / 100)/10.0 + " K";
		if (k < 1000000) return (k / 1000) + " K";
		if (k < 10000000) return (k / 10000)/100.0 + " M";
		if (k < 100000000) return (k / 100000)/10.0 + " M";
		if (k < 1000000000) return (k / 1000000) + " M";
		return "INF K";
	}
}
