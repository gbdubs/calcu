package calculus.models;

import java.util.ArrayList;
import java.util.List;

import calculus.api.PracticeProblemAPI;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
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
		super(entity, "practiceProblem");
	}
	
	public PracticeProblem (Key key) {
		super(key, "practiceProblem");
	}
	
	public PracticeProblem (String uuid) {
		super(uuid, "practiceProblem");
	}
	
	public String getAuthorSolution(){
		return ((Text) getEntity().getProperty("authorSolution")).getValue();
	}
	
	public String getEditUrl(){
		return "/contribute/practice-problem/edit/" + this.getUuid();
	}
	
	public List<Answer> getAnswers(){
		return PracticeProblemAPI.getAnswersForPracticeProblem(this);
	}
	
	public String getNewAnswerUploadUrl(){
		return "/answer/" + this.getUuid();
	}
	
	public void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property modification.");
	}

}
