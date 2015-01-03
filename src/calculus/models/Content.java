package calculus.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import calculus.utilities.KarmaDescription;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Text;

public abstract class Content {
	
	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	protected static List<String> FIELDS = new ArrayList<String>();
	
	static {
		String[] fields = {"contentType", "uuid", "creatorUserId", "createdAt", "title", "body", "anonymous", "submitted", "viewable", "url", "karma"};
		for (String s : fields) {FIELDS.add(s);}
	}

	private Entity entity;
	private Key key;
	private Author author;
	
	public Content (Entity entity, String contentType) {
		this.entity = entity;
		this.key = entity.getKey();
		this.entity.setProperty("contentType", contentType);
		this.entity.setProperty("karma", 0);
		this.author = null;
	}
	
	public Content (Key key, String contentType) {
		this.key = key;
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = new Entity(key);
		}
		this.entity.setProperty("contentType", contentType);
		this.entity.setProperty("karma", 0);
		this.author = null;
	}

	public Content (String uuid, String contentType) {
		this.key = KeyFactory.createKey("Content", uuid);
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = new Entity(key);
		}
		this.entity.setProperty("contentType", contentType);
		this.entity.setProperty("karma", 0);
		this.author = null;
	}
	
	public void refresh(){
		this.key = this.entity.getKey();
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
	
	public abstract void verifyAcceptableProperty(String property);
	
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
	
	public static String getContentType(String uuid){
		Query q = new Query("Content").addFilter("uuid", FilterOperator.EQUAL, uuid);
		PreparedQuery pq = datastoreService.prepare(q);
		Entity entity = pq.asSingleEntity();
		return (String) entity.getProperty("contentType");
	}
}
