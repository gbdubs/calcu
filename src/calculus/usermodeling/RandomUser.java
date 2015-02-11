package calculus.usermodeling;

import java.util.ArrayList;
import java.util.List;

import calculus.api.ContentAPI;
import calculus.api.RatingsAPI;
import calculus.api.TagAPI;
import calculus.models.Content;
import calculus.recommendation.InterestsAPI;
import calculus.recommendation.PhenotypeAPI;
import calculus.recommendation.SkillsAPI;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.taskqueue.TaskOptions;

public class RandomUser {

	private static int randomUserId = 0;
	
	public static void execute(String interests, String numberOfActions, String relativeSkill){
		int n = Integer.parseInt(numberOfActions);
		int skillLevel = Integer.parseInt(relativeSkill);
		List<String> tags = TagAPI.getTagsFromString(interests);
		
		String userId = "RandomUser" + randomUserId++;
		
		while (n-- > 0){
			double instruction = Math.random();
			
			if (instruction <  .1){
				List<String> interested = new ArrayList<String>();
				List<String> disinterested = new ArrayList<String>();
				for(int i = 0; i < 3; i++){
					interested.add(TagAPI.randomTag());
					disinterested.add(TagAPI.randomTag());
				}
				tags.addAll(interested);
				InterestsAPI.userIndicatedTagsInterestingInCalibration(userId, tags);
				InterestsAPI.userIndicatedTagsDisinterestingInCalibration(userId, disinterested);
			
			} else if (instruction <  .9){
				String search = interests + "," + TagAPI.randomTag() + "," + TagAPI.randomTag();
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
					if (Math.random() > .3){
						RatingsAPI.submitRating(uuid, userId, (int)(Math.random() * 20 + 80), 100 - skillLevel, (int) (Math.random() * 100));
					}
				}
				
			} else if (instruction <  .2){
				int i = (int) (Math.random() * PhenotypeAPI.DEFAULT_PHENOTYPE.length());
				if (Math.random() > .5){
					i += 65;
				} else{
					i += 97;
				}
				PhenotypeAPI.updateUserPhenotype(userId, (char) i);
			} else {
				// SKip this  round.
			}
		}
	}
	
	
	public static TaskOptions createNewRandomUser(){
		TaskOptions to = TaskOptions.Builder.withUrl("/admin/tasks/simulate-random-user")
			.param("interests", RandomUser.randomIntersts())
			.param("numberOfActions", RandomUser.randomNumberOfActions())
			.param("relativeSkill", RandomUser.randomSkill());
		return to;
	}

	private static String randomSkill() {
		return "" + (int) (Math.random() * 100);
	}

	private static String randomNumberOfActions() {
		return "" + (int) (Math.random() * 200);
	}

	private static String randomIntersts() {
		Content randomContent = ContentAPI.getRandomContent(1).get(0);
		String tags = randomContent.getTags();
		List<String> contentUuids = TagAPI.getUuidsResultsOfMultipleTags(tags, 10, (int) (Math.random() * 20));
		List<String> allTags = new ArrayList<String>();
		for(String uuid : contentUuids){
			try {
				Content c = new Content(uuid);
				allTags.addAll(TagAPI.getTagsFromString(c.getTags()));
			} catch (EntityNotFoundException e) {
				// Skip
			}
		}
		String result = "";
		for(String tag : allTags){
			result = result + "," + tag;
		}
		return result.substring(1);
	}
}
