package utilities;

import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PracticeProblem {

	Entity entity;

	private Key key;
	
	static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	static UserService userService = UserServiceFactory.getUserService();
	
	private static final String[] FIELDS = {
		"practiceProblemId", 
		"creatorUserId", 
		"createdAt", 
		"problemTitle", 
		"problemBody", 
		"authorSolution", 
		"otherSolutions", 
		"anonymous", 
		"submitted", 
		"viewable", 
		"url"
	};
		
	public PracticeProblem (Entity entity) {
		this.entity = entity;
		this.key = entity.getKey();
	}
	
	public PracticeProblem (Key key) {
		this.key = key;
		try {
			this.entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			this.entity = null;
		}
	}
	
	public PracticeProblem (String uuid) {
		this.key = KeyFactory.createKey("PracticeProblem", uuid);
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
			datastoreService.put(entity);
		}
	}
	
	public void updateProperty(String property, String newValue) {
		verifyAcceptableProperty(property);
		
		if (entity.getProperty(property) != newValue){
			entity.setProperty(property, newValue);
			datastoreService.put(entity);
		}
	}
	
	private static void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property modification.");
	}
	
	public String getProperty(String propertyName) {
		verifyAcceptableProperty(propertyName);
		return (String) this.entity.getProperty(propertyName);
	}

	public String getPracticeProblemId(){
		return (String) entity.getProperty("practiceProblemId");
	}
	
	public String getCreatorUserId(){
		return (String) entity.getProperty("creatorUserId");
	} 
	
	public String getCreatedAt(){
		return (String) entity.getProperty("createdAt");
	} 
	
	public String getProblemTitle(){
		return (String) entity.getProperty("problemTitle");
	} 
	
	public String getProblemBody(){
		return (String) entity.getProperty("problemBody");
	} 
	
	public String getAuthorSolution(){
		return (String) entity.getProperty("authorSolution");
	} 
	
	public String getOtherSolutions(){
		return (String) entity.getProperty("otherSolutions");
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
	
	public String getEditUrl(){
		return "/contribute/practice-problem/edit/" + this.entity.getProperty("practiceProblemId");
	}

	
}
