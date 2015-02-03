package calculus.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import calculus.api.ContentAPI;
import calculus.utilities.KarmaDescription;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class Content {
	
	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	protected static List<String> FIELDS = new ArrayList<String>();
	protected static List<String> CONTENT_TYPES = new ArrayList<String>();
	public static String scrapingUserProfileId = "11481431651082296168";
	
	static {
		String[] fields = {"contentType", "uuid", "creatorUserId", "createdAt", "title", "body", "anonymous", "submitted", "viewable", "url", "karma", "tags"};
		String[] contentTypes = {"practiceProblem", "question", "answer", "textContent"};
		for (String s : fields) {FIELDS.add(s);}
		for (String s : contentTypes) {CONTENT_TYPES.add(s);}
	}

	private Entity entity;
	private Author author;
	
	public Content (Entity entity, String contentType) {
		if (!CONTENT_TYPES.contains(contentType)) throw new RuntimeException("The content type ["+contentType+"] is not a recognized type.");
		this.entity = entity;
		this.entity.setProperty("contentType", contentType);
		this.author = null;
	}

	public Content (String uuid, String contentType) {
		Key key = KeyFactory.createKey("Content", uuid);
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = new Entity(key);
			this.entity.setProperty("contentType", contentType);
			this.author = null;
		}
	}

	public Content(String uuid) throws EntityNotFoundException {
		this(uuid, ContentAPI.getContentType(uuid));
	}

	public Content(Entity e) {
		this(e, (String) e.getProperty("contentType"));
	}

	public void refresh(){
		Key key = this.entity.getKey();
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = null;
		}
	}
	
	public void updateProperty(String property, Object newValue) {
		verifyAcceptableProperty(property);
		if (entity.getProperty(property) != newValue){
			entity.setProperty(property, newValue);
			datastoreService.put(entity);
		}
	}
	
	//TODO: This refactor from an abstract class.
	public void verifyAcceptableProperty(String property){

	};
	
	public String getContentType(){
		return (String) entity.getProperty("contentType");
	}
	
	public String getUuid(){
		return (String) entity.getProperty("uuid");
	}
	
	public String getKarma(){
		return "" + entity.getProperty("karma");
	}
	
	public String getReadableKarma(){
		return KarmaDescription.toMediumString(((Long) entity.getProperty("karma")).intValue());
	}
	
	public String getCreatorUserId(){
		return (String) entity.getProperty("creatorUserId");
	}
	
	public Author getAuthor(){
		if (author == null)
			author = new Author(getCreatorUserId());
		return this.author;
	}
	
	public long getCreatedAt(){
		return (long) entity.getProperty("createdAt");
	}
	
	public String getTitle(){
		return (String) entity.getProperty("title");
	}
	
	public String getBody(){
		return ((Text) entity.getProperty("body")).getValue();
	}
	
	public boolean getAnonymous(){
		return (boolean) entity.getProperty("anonymous");
	}
	
	public boolean getSubmitted(){
		return (boolean) entity.getProperty("submitted");
	} 
	
	public boolean getViewable(){
		return (boolean) entity.getProperty("viewable");
	}
	
	public String getUrl(){
		return (String) entity.getProperty("url");
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public String getRateUrl(){
		return "/rate/" + entity.getProperty("uuid");
	}
	
	public String getReportUrl(){
		return "/report/" + entity.getProperty("uuid");
	}
	
	public String getReadableTime(){
		Date d = new Date(this.getCreatedAt());
		SimpleDateFormat df = new SimpleDateFormat("MM/DD/YY HH:mm");
		return df.format(d);
	}
	
	public String getShortReadableTime(){
		Date d = new Date(this.getCreatedAt());
		SimpleDateFormat df = new SimpleDateFormat("MM/DD/YY");
		return df.format(d);
	}
	
	public String getTags(){
		String s = (String) entity.getProperty("tags");
		if (s == null) return "";
		return s;
	}
	
	public String getReadableTags(){
		String s = (String) entity.getProperty("tags");
		if (s == null) return "";
		return s.replace(",", ", ");
	}
	
	public String getAbbreviatedBody(){
		String body = this.getBody();
		if (body.length() > 300){
			return body.substring(0, 297) + "...";
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
	
	public String getBoxColor(){
		String contentType = (String) entity.getProperty("contentType");
		return ContentAPI.getBoxColor(contentType);
	}
	
	public String getBoxIcon(){
		String contentType = (String) entity.getProperty("contentType");
		return ContentAPI.getBoxIcon(contentType);
	}

	public void incrementKarma(int differential) {
		long karma = (long) this.entity.getProperty("karma");
		this.entity.setProperty("karma", karma + differential);
		datastoreService.put(entity);
	}
}
