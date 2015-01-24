package calculus.models;

import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Achievement {

	private Entity entity;
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public Achievement(String uuid){
		try {
			entity = datastore.get(KeyFactory.createKey("Achievement", uuid));
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Achievement with UUID ["+uuid+"] does not exist.");
		}
	}
	
	public String getName(){
		return (String) entity.getProperty("name");
	}
	
	public String getIcon(){
		return (String) entity.getProperty("icon");
	}
	
	public String getColor(){
		return (String) entity.getProperty("color");
	}
	
	public String getSecondaryColor(){
		return (String) entity.getProperty("secondaryColor");
	}
	
	public String getDescription(){
		return (String) entity.getProperty("description");
	}
	
	public String getQualification(){
		return (String) entity.getProperty("qualification");
	}
	
	public String getUuid(){
		return (String) entity.getProperty("uuid");
	}
}
