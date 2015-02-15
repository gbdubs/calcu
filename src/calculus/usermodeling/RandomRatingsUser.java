package calculus.usermodeling;

import java.util.ArrayList;
import java.util.List;

import calculus.api.ContentAPI;
import calculus.api.RandomValuesAPI;
import calculus.api.RatingsAPI;
import calculus.api.TagAPI;
import calculus.models.Content;
import calculus.recommendation.InterestsAPI;
import calculus.recommendation.PhenotypeAPI;
import calculus.recommendation.SkillsAPI;

import com.google.appengine.api.taskqueue.TaskOptions;

public class RandomRatingsUser {

	public static void executeRatingsOprations(String interests, String numberOfActions, String relativeSkill){
		executeRatingsOperations(interests, numberOfActions, relativeSkill, "" + (int) (Math.random() * 1000));
	}
	
	public static void executeRatingsOperations(String interests, String numberOfActions, String relativeSkill, String id){
		int n = Integer.parseInt(numberOfActions);
		int skillLevel = Integer.parseInt(relativeSkill);
		List<String> tags = TagAPI.getTagsFromString(interests);
		
		String userId = "RandomUser" + id;
		long start = System.currentTimeMillis();

		int interestCalibration = 0;
		int searches = 0;
		int phenotyped = 0;
		
		while (n-- > 0){
			double instruction = Math.random();
			if (instruction <  .1){
				interestCalibration++;
				List<String> interested = new ArrayList<String>();
				List<String> disinterested = new ArrayList<String>();
				interested.addAll(RandomValuesAPI.randomTags(3));
				disinterested.addAll(RandomValuesAPI.randomTags(3));
				tags.addAll(interested);
				InterestsAPI.userIndicatedTagsInterestingInCalibration(userId, tags);
				InterestsAPI.userIndicatedTagsDisinterestingInCalibration(userId, disinterested);
			} else if (instruction <  .8){
				searches++;
				String search = interests + "," + RandomValuesAPI.randomTag() + "," + RandomValuesAPI.randomTag();
				List<String> uuids = TagAPI.getUuidsResultsOfMultipleTags(search, 10, (int) (Math.random() * 40));
				InterestsAPI.userSearchedForTags(userId, TagAPI.getTagsFromString(search));
				for(String uuid : uuids){
					if (Math.random() > .5){
						InterestsAPI.userViewedContent(userId, uuid);
					}
					if (Math.random() > .7){
						InterestsAPI.userBookmarkedContent(userId, uuid);
					}
					if (Math.random() > .8){
						InterestsAPI.userAnsweredContent(userId, uuid);
						SkillsAPI.userAnsweredContent(userId, uuid);
					}
					if (Math.random() > .6){
						RatingsAPI.submitRating(uuid, userId, (int)(Math.random() * 20 + 80), 100 - skillLevel, (int) (Math.random() * 100));
					}
				}
				
			} else {
				phenotyped++;
				int i = (int) (Math.random() * PhenotypeAPI.DEFAULT_PHENOTYPE.length());
				if (Math.random() > .5){
					i += 65;
				} else{
					i += 97;
				}
				PhenotypeAPI.updateUserPhenotype(userId, (char) i);
			}
		}
		
		System.out.println(
				userId + " completed execution after " + (System.currentTimeMillis() - start) + " milliseconds." +
				"Phenotyped: " + phenotyped + " Searched: " + searches + " InterestCalibration: " + interestCalibration
		);
	}
	
	
	public static TaskOptions createNewRandomUser(){
		TaskOptions to = TaskOptions.Builder.withUrl("/admin/tasks/simulate-random-user")
			.param("interests", randomIntersts())
			.param("numberOfActions", randomNumberOfActions())
			.param("relativeSkill", randomSkill());
		return to;
	}

	private static String randomSkill() {
		return "" + (int) (Math.random() * 100);
	}

	private static String randomNumberOfActions() {
		return "" + (int) (Math.random() * 100);
	}

	private static String randomIntersts() {
		Content randomContent = RandomValuesAPI.randomContent();
		String tags = randomContent.getTags();
		List<String> contentUuids = TagAPI.getUuidsResultsOfMultipleTags(tags, 10, (int) (Math.random() * 20));
		List<String> allTags = new ArrayList<String>();
		List<Content> content = ContentAPI.getContentAsync(contentUuids);
		for(Content c : content){
			allTags.addAll(TagAPI.getTagsFromString(c.getTags()));
		}
		String result = "";
		for(int i = 0; i < allTags.size() && i < 10; i++){
			String tag = allTags.get(i);
			result = result + "," + tag;
		}
		return result.substring(1);
	}
}
