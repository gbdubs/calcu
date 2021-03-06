package calculus.models;

import calculus.upload.UploadServlet;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class Achievement {

	private String icon;
	private String qualification;
	private String color;
	private String description;
	private String name;
	private String uuid;
	private String secondaryColor;
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	
	// GSON CONSTRUCTOR. DO NOT INVOKE.
	public Achievement(){
		
	}
	
	public Achievement(String uuid){
		Entity entity = null;
		try {
			entity = datastore.get(KeyFactory.createKey("Achievement", uuid));
		} catch (EntityNotFoundException e) {
			UploadServlet.uploadAchievements();
			throw new RuntimeException("Achievement with UUID ["+uuid+"] does not exist.");
		}
		this.uuid = uuid;
		this.icon = (String) entity.getProperty("icon");
		this.color = (String) entity.getProperty("color");
		this.secondaryColor = (String) entity.getProperty("secondaryColor");
		this.description = (String) entity.getProperty("description");
		this.name = (String) entity.getProperty("name");
		this.qualification = (String) entity.getProperty("qualification");
 	}
	
	public Achievement(Entity entity){
		this.uuid = (String) entity.getProperty("uuid");
		this.icon = (String) entity.getProperty("icon");
		this.color = (String) entity.getProperty("color");
		this.secondaryColor = (String) entity.getProperty("secondaryColor");
		this.description = (String) entity.getProperty("description");
		this.name = (String) entity.getProperty("name");
		this.qualification = (String) entity.getProperty("qualification");
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getIcon(){
		return this.icon;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public String getSecondaryColor(){
		return this.secondaryColor;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getQualification(){
		return this.qualification;
	}
	
	public String getUuid(){
		return this.uuid;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Achievement){
			Achievement a = (Achievement) o;
			if (a.getUuid().equals(this.getUuid())){
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void saveAsync() {
		Entity e = new Entity(KeyFactory.createKey("Achievement", uuid));
		
		e.setProperty("uuid", uuid);
		e.setProperty("icon", icon);
		e.setProperty("color", color);
		e.setProperty("secondaryColor", secondaryColor);
		e.setProperty("description", description);
		e.setProperty("qualification", qualification);
		e.setProperty("name", name);
		
		asyncDatastore.put(e);
		
	}
}
