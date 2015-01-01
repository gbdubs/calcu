package calculus.models;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PracticeProblem extends Content{

	static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	static UserService userService = UserServiceFactory.getUserService();
	
	private static List<String> FIELDS = new ArrayList<String>();
	
	static {
		FIELDS.addAll(Content.FIELDS);
		FIELDS.add("authorSolution");
		FIELDS.add("otherSolutions");
	}
			
	public PracticeProblem (Entity entity) {
		super(entity);
		this.updateProperty("contentType", "practiceProblem");
	}
	
	public PracticeProblem (Key key) {
		super(key);
		this.updateProperty("contentType", "practiceProblem");
	}
	
	public PracticeProblem (String uuid) {
		super(uuid);
		this.updateProperty("contentType", "practiceProblem");
	}
	
	public String getAuthorSolution(){
		return (String) getEntity().getProperty("authorSolution");
	}
	
	public String getOtherSolutions(){
		return (String) getEntity().getProperty("otherSolutions");
	}
	
	public String getEditUrl(){
		return "/contribute/practice-problem/edit/" + this.getUuid();
	}
	
	public void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property modification.");
	}

}
