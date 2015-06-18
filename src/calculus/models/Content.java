package calculus.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import calculus.api.AchievementsAPI;
import calculus.api.ContentAPI;
import calculus.api.RatingsAPI;
import calculus.utilities.Cleaner;
import calculus.utilities.KarmaDescription;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class Content {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static UserService userService = UserServiceFactory.getUserService();
	
	protected static List<String> CONTENT_TYPES = new ArrayList<String>();
	
	static {
		String[] contentTypes = {"practiceProblem", "question", "answer", "textContent"};
		for (String s : contentTypes) {CONTENT_TYPES.add(s);}
	}
	
	private Entity entity;
	private Author author;
	
	private String contentType;
	private String uuid;
	private String creatorUserId;
	private long createdAt;
	private String title;
	private String body;
	private boolean anonymous;
	private boolean submitted;
	private boolean viewable;
	private String url;
	private int karma;
	private String tags;
	private List<String> allAnswers;
	
	public Content (Entity entity, String contentType) {
		if (!CONTENT_TYPES.contains(contentType)) throw new RuntimeException("The content type ["+contentType+"] is not a recognized type.");
		
		this.contentType = contentType;
		this.uuid = (String) entity.getProperty("uuid");
		this.creatorUserId = (String) entity.getProperty("creatorUserId");
		this.createdAt = (Long) entity.getProperty("createdAt");
		this.title = (String) entity.getProperty("title");
		this.body = (String) entity.getProperty("body");
		this.anonymous = (boolean) entity.getProperty("anonymous");
		this.submitted = (boolean) entity.getProperty("submitted");
		this.viewable = (boolean) entity.getProperty("viewable");
		this.url = (String) entity.getProperty("url");
		this.karma = ((Long) entity.getProperty("karma")).intValue();
		this.tags = (String) entity.getProperty("tags");
		this.allAnswers = (List<String>) entity.getProperty("allAnswers");
		
		this.entity = entity;
		this.author = null;
	}

	public Content(Entity e) {
		this(e, (String) e.getProperty("contentType"));
	}

	public Content (String uuid, String contentType) throws EntityNotFoundException{
		this(datastore.get(KeyFactory.createKey("Content", uuid)), contentType);
	}

	public Content(String uuid) throws EntityNotFoundException {
		this(datastore.get(KeyFactory.createKey("Content", uuid)), ContentAPI.getContentType(uuid));
	}
	
	public String getContentType(){
		return contentType;
	}
	
	public String getUuid(){
		return uuid;
	}
	
	public String getKarma(){
		return "" + karma;
	}
	
	public String getReadableKarma(){
		return KarmaDescription.toMediumString(karma);
	}
	
	public String getCreatorUserId(){
		return creatorUserId;
	}
	
	public Author getAuthor(){
		if (author == null){
			author = new Author(creatorUserId);
		}
		return this.author;
	}
	
	public long getCreatedAt(){
		return createdAt;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getBody(){
		return body;
	}
	
	public boolean getAnonymous(){
		return anonymous;
	}
	
	public boolean getSubmitted(){
		return submitted;
	} 
	
	public boolean getViewable(){
		return viewable;
	}
	
	public String getUrl(){
		return url;
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public String getRateUrl(){
		return "/rate/" + uuid;
	}
	
	public String getReportUrl(){
		return "/report/" + uuid;
	}
	
	public String getReadableTime(){
		Date d = new Date(this.getCreatedAt());
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/YY HH:mm");
		return df.format(d);
	}
	
	public String getShortReadableTime(){
		Date d = new Date(this.getCreatedAt());
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/YY");
		return df.format(d);
	}
	
	public String getTags(){
		if (tags == null) return "";
		return tags;
	}
	
	public String getReadableTags(){
		if (tags == null) return "";
		return tags.replace(",", ", ");
	}
	
	public List<String> getListOfTags(){
		List<String> allTags = new ArrayList<String>();
		if (tags == null || tags.length() == 0) return allTags;
		String[] tagsList = tags.split(",");
		for(String tag : tagsList){
			if (!tag.equals("")){
				allTags.add(tag);
			}
		}
		return allTags;
	}
	
	public String getAbbreviatedBody(){
		String body = this.getBody();
		if (body.length() > 300){
			String cleanedBody = Cleaner.cleanHtml(body.substring(0, 297));
			return cleanedBody + "...";
		} else {
			return body;
		}
	}
	
	public String getNewAnswerUploadUrl(){
		return "/create-answer/" + this.getUuid();
	}
	
	public String getPreviewUrl(){
		return "/contribute/preview/" + this.getUuid();
	}
	
	public String toString(){
		return this.entity.toString();
	}
	
	public int stableRandom(int modulo){
		return (int) (createdAt % modulo);
	}

	public String getBoxColor(){
		return ContentAPI.getBoxColor(contentType);
	}
	
	public String getBoxIcon(){
		return ContentAPI.getBoxIcon(contentType);
	}

	public void incrementKarma(int differential) {
		this.karma += differential;
		if (karma > this.getBody().length()){
			String authorId = this.getCreatorUserId();
			AchievementsAPI.brevityAchievement(authorId);
		}
		this.save();
	}

	public boolean getAlreadyRatedByCurrentUser(){
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) return false;
		return RatingsAPI.contentRatedByUser(this.getUuid(), user.getUserId());
	}
	
	public List<Answer> getAnswers(){
		if (allAnswers == null) return new ArrayList<Answer>();
		List<Answer> result = new ArrayList<Answer>();
		for(String uuid : allAnswers){
			Answer answer = new Answer(uuid);
			if (!answer.getViewable()){
				if (userService.isUserLoggedIn()){
					if (userService.isUserAdmin()){
						result.add(answer);
					}
				}
			} else {
				result.add(answer);
			}
		}
		return result;
	}

	public void addAnswer(String answerUuid) {
		if (allAnswers == null) allAnswers = new ArrayList<String>();
		allAnswers.add(answerUuid);
		entity.setUnindexedProperty("allAnswers", allAnswers);
		save();
	}

	public void removeAnswer(String answerUuid) {
		if (allAnswers == null) return;
		if (allAnswers.contains(answerUuid)){
			allAnswers.remove(answerUuid);
			entity.setUnindexedProperty("allAnswers", allAnswers);
			save();
		}
	}
	
	public void save(){
		entity.setProperty("contentType", contentType);
		entity.setProperty("uuid", uuid);
		entity.setProperty("creatorUserId", creatorUserId);
		entity.setProperty("createdAt", createdAt);
		entity.setProperty("title", title);
		entity.setProperty("body", body);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", url);
		entity.setProperty("karma", new Long(karma));
		entity.setProperty("tags", tags);
		entity.setProperty("allAnswers", allAnswers);
		
		this.setTypeSpecificEntityProperties();
		
		datastore.put(entity);
	}
	
	public abstract void setTypeSpecificEntityProperties();
	
}
