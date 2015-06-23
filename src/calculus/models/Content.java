package calculus.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import calculus.api.AchievementsAPI;
import calculus.api.ContentAPI;
import calculus.api.RatingsAPI;
import calculus.api.TagAPI;
import calculus.utilities.Cleaner;
import calculus.utilities.KarmaDescription;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class Content {
	
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static UserService userService = UserServiceFactory.getUserService();
	
	protected static List<String> CONTENT_TYPES;
	
	static {
		CONTENT_TYPES = new ArrayList<String>();
		String[] contentTypes = {"practiceProblem", "question", "answer", "textContent"};
		for (String s : contentTypes) {CONTENT_TYPES.add(s);}
	}
	
	// We have these as transient fields so they are not saved in the JSON Serialization process.
	protected transient Entity entity;
	private transient Author author;
	private transient String previousTags;
	
	private String uuid;
	private String contentType;
	private String creatorUserId;
	private long createdAt;
	private boolean anonymous;
	private boolean submitted;
	private boolean viewable;
	private int karma;
	private String title;
	private String body;
	private List<String> allAnswers;
	private String tags;
	private String url;
	private String source;
	
	/*
	 * Each of the following properties is either UNINDEXED (meaning we don't perform queries over it, but
	 * we save some write costs), or INDEXED (which is only necessary if we want to perform any queries over
	 * it).
	 * 
	 * uuid           = UNINDEXED
	 * contentType    = INDEXED
	 * creatorUserId  = INDEXED
	 * createdAt      = INDEXED	 
	 * anonymous      = UNINDEXED
	 * submitted      = INDEXED
	 * viewable       = INDEXED
	 * karma          = INDEXED
	 * title          = UNINDEXED
	 * body           = UNINDEXED
	 * allAnswers     = UNINDEXED
	 * tags           = UNINDEXED
	 * url            = UNINDEXED
	 * source         = UNINDEXED
	 * 
	 * SUBCLASSES:
	 * authorSolution = UNINDEXED [practiceProblem only]
	 * approved       = UNINDEXED [answer only]
	 * parentUuid     = UNINDEXED [answer only]
	 */
	
	public Content (Entity entity, String contentType) {
		if (!CONTENT_TYPES.contains(contentType)) throw new RuntimeException("The content type ["+contentType+"] is not a recognized type.");
		
		this.uuid = (String) entity.getProperty("uuid");
		this.contentType = contentType;
		this.creatorUserId = (String) entity.getProperty("creatorUserId");
		this.createdAt = (Long) entity.getProperty("createdAt");
		this.anonymous = (boolean) entity.getProperty("anonymous");
		this.submitted = (boolean) entity.getProperty("submitted");
		this.viewable = (boolean) entity.getProperty("viewable");
		this.karma = ((Long) entity.getProperty("karma")).intValue();
		this.title = (String) entity.getProperty("title");
		this.body = ((Text) entity.getProperty("body")).getValue();
		this.allAnswers = (List<String>) entity.getProperty("allAnswers");
		this.tags = (String) entity.getProperty("tags");
		this.url = (String) entity.getProperty("url");
		this.source = (String) entity.getProperty("source");
		
		this.entity = entity;
		this.author = null;
		this.previousTags = tags;
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
	
	public String getUuid(){
		return uuid;
	}
	
	public String getContentType(){
		return contentType;
	}

	public String getBoxColor(){
		return ContentAPI.getBoxColor(contentType);
	}

	public String getBoxIcon(){
		return ContentAPI.getBoxIcon(contentType);
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

	public boolean getAnonymous(){
		return anonymous;
	}

	public boolean getSubmitted(){
		return submitted;
	}

	public boolean getViewable(){
		return viewable;
	}

	public String getKarma(){
		return "" + karma;
	}
	
	public void incrementKarma(int differential) {
		this.karma += differential;
		if (karma > this.getBody().length()){
			String authorId = this.getCreatorUserId();
			AchievementsAPI.brevityAchievement(authorId);
		}
		this.save();
	}

	public String getReadableKarma(){
		return KarmaDescription.toMediumString(karma);
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getBody(){
		return body;
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

	public List<String> getAnswerUuids(){
		return allAnswers;
	}
	
	public List<Answer> getAnswers(){
		if (allAnswers == null) return new ArrayList<Answer>();
		List<Answer> result = new ArrayList<Answer>();
		for(String uuid : allAnswers){
			Answer answer;
			try {
				answer = new Answer(uuid);
				if (!answer.getViewable()){
					if (userService.isUserLoggedIn()){
						if (userService.isUserAdmin()){
							result.add(answer);
						}
					}
				} else {
					result.add(answer);
				}
			} catch (EntityNotFoundException e) {
				// Don't include it if it doesn't exit! :)
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

	public String getNewAnswerUploadUrl(){
		return "/create-answer/" + this.getUuid();
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

	public String getUrl(){
		return url;
	}
	
	public String getRateUrl(){
		return "/rate/" + uuid;
	}
	
	public String getReportUrl(){
		return "/report/" + uuid;
	}
	
	public String getPreviewUrl(){
		return "/contribute/preview/" + this.getUuid();
	}

	public String getSource(){
		return source;
	}
	
	public boolean getAlreadyRatedByCurrentUser(){
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) return false;
		return RatingsAPI.contentRatedByUser(this.getUuid(), user.getUserId());
	}

	public String toString(){
		return this.entity.toString();
	}

	public int stableRandom(int modulo){
		return (int) (createdAt % modulo);
	}

	private void saveTags(){
		if (!tags.equals(previousTags)){
			List<String> toRemove = Arrays.asList(previousTags.split(","));
			List<String> toAdd = Arrays.asList(tags.split(","));
			Set<String> intersection = new HashSet<String>(toAdd);
			intersection.retainAll(toRemove);
			toAdd.removeAll(intersection);
			toRemove.removeAll(intersection);
		
			for (String tag : toRemove){
				TagAPI.removeContentFromTag(uuid, tag);
			}
			
			for (String tag : toAdd){
				TagAPI.addNewContentToTag(uuid, tag);
			}
		}
	}
	
	private void preSave(){
		entity.setUnindexedProperty("uuid", uuid);
		entity.setProperty("contentType", contentType);
		entity.setProperty("creatorUserId", creatorUserId);
		entity.setProperty("createdAt", createdAt);
		entity.setUnindexedProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("karma", new Long(karma));
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", new Text(body));
		entity.setUnindexedProperty("allAnswers", allAnswers);
		entity.setUnindexedProperty("tags", tags);
		entity.setUnindexedProperty("url", url);
		entity.setUnindexedProperty("source", source);
		
		saveTags();
		
		setTypeSpecificEntityProperties();
	}
	
	public void save(){
		preSave();
		datastore.put(entity);
	}
	
	public void saveAsync() {
		preSave();
		asyncDatastore.put(entity);
	}

	public abstract void setTypeSpecificEntityProperties();
	
}
