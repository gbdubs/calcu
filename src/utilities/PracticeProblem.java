package utilities;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PracticeProblem {

	private Entity entity;

	private Key key;
	
	private static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	private static UserService userService = UserServiceFactory.getUserService();
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
	
	public static PracticeProblem newPracticeProblemFromRequest(HttpServletRequest req) {

		String uuid = UUID.randomUUID().toString();
		String dateAndTime = (new Date()).toString();

		String anonymous = ((Boolean) (req.getParameter("anonymousSubmit") != null)).toString();
		String submitted = ((Boolean) (req.getParameter("saveWork") == null)).toString();
		String viewable = ((Boolean) (req.getParameter("saveWork") == null)).toString();
		
		String problemTitle = (String) req.getParameter("problemTitle");
		if (problemTitle == null || problemTitle == "") problemTitle = "[Un-named Problem]";
		String problemBody = (String) req.getParameter("problemBody");
		if (problemBody == null || problemBody == "") problemBody = "[The biggest problem of all: nothing]";
		String authorSolution = (String) req.getParameter("authorSolution");
		if (authorSolution == null || authorSolution == "") authorSolution = "[The Author has not provided an answer to this problem]";
		
		
		Entity entity = new Entity(KeyFactory.createKey("PracticeProblem", uuid));
		
		entity.setProperty("practiceProblemId", uuid);
		entity.setProperty("creatorUserId", userService.getCurrentUser().getUserId());
		entity.setProperty("createdAt", dateAndTime);
		entity.setProperty("problemTitle", problemTitle);
		entity.setProperty("problemBody", problemBody);
		entity.setProperty("authorSolution", authorSolution);
		entity.setProperty("otherSolutions", "<solutions></solutions>");
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", "/practice-problems/" + uuid);
		
		datastoreService.put(entity);
		
		return new PracticeProblem(entity);
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
