package calculus.api;

public class RatingsAPI {

	public static void submitRating(String contentUuid, String userId, int helpfulness, int difficulty, int quality) {
		System.out.println("Rating Submitted for ["+contentUuid+"] by ["+userId+"]: H=["+helpfulness+"], D=["+difficulty+"], Q=["+quality+"]");
		KarmaAPI.updateKarmaFromRanking(contentUuid, helpfulness, difficulty, quality);
	}

}
