package calculus.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class Content {
	
	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	private UserService userService = UserServiceFactory.getUserService();
	protected static List<String> FIELDS = new ArrayList<String>();
	
	static {
		String[] fields = {"contentType", "uuid", "creatorUserId", "createdAt", "title", "body", "anonymous", "submitted", "viewable", "url"};
		for (String s : fields) {FIELDS.add(s);}
	}

	private Entity entity;
	private Key key;
	
	public Content (Entity entity) {
		this.entity = entity;
		this.key = entity.getKey();
	}
	
	public Content (Key key) {
		this.key = key;
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = null;
		}
	}

	public Content (String uuid) {
		this.key = KeyFactory.createKey("Content", uuid);
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = null;
		}
	}
	
	public void refresh(){
		this.key = this.entity.getKey();
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = null;
		}
	}
	
	public void updateProperties(Map<String, String> newValues) {
		boolean changed = false;
		for(String property : newValues.keySet()){
			verifyAcceptableProperty(property);
			String newValue = newValues.get(property);
			if (entity.getProperty(property) != newValue){
				entity.setProperty(property, newValue);
				changed = true;
			}
		}
		if (changed){
			datastoreService.put(this.entity);
		}
	}
	
	
	public void updateProperty(String property, String newValue) {
		verifyAcceptableProperty(property);
		if (entity.getProperty(property) != newValue){
			entity.setProperty(property, newValue);
			datastoreService.put(entity);
		}
	}
	
	public String getProperty(String propertyName) {
		verifyAcceptableProperty(propertyName);
		return (String) entity.getProperty(propertyName);
	}
	
	public abstract void verifyAcceptableProperty(String property);
	
	public String getContentType(){
		return (String) entity.getProperty("contentType");
	}
	
	public String getUuid(){
		return (String) entity.getProperty("uuid");
	}
	
	public String getCreatorUserId(){
		return (String) entity.getProperty("creatorUserId");
	}
	
	public String getCreatedAt(){
		return (String) entity.getProperty("createdAt");
	}
	
	public String getTitle(){
		return (String) entity.getProperty("title");
	}
	
	public String getBody(){
		return (String) entity.getProperty("body");
	}
	
	public String getAnonymous(){
		return (String) entity.getProperty("anonymous");
	}
	
	public String getSubmitted(){
		return (String) entity.getProperty("submitted");
	} 
	
	public String getViewable(){
		return (String) entity.getProperty("viewable");
	}
	
	public String getUrl(){
		return (String) entity.getProperty("url");
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public String getReadableTime(){
		Long l = Long.parseLong(this.getCreatedAt());
		Date d = new Date(l);
		SimpleDateFormat df = new SimpleDateFormat("MM/DD/YY HH:mm");
		return df.format(d);
	}	
}
