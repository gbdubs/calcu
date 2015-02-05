package calculus.models;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Answer extends Content {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static List<String> FIELDS = new ArrayList<String>();
	
	static {
		FIELDS.addAll(Content.FIELDS);
		FIELDS.add("parentUuid");
		FIELDS.add("approved");
	}
	
	public Answer(String uuid) {
		super(uuid, "answer");
	}
	
	public Answer(Entity entity) {
		super(entity, "answer");
	}
	
	@Override
	public void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property modification.");
	}

	public boolean getApproved(){
		return (boolean) this.getEntity().getProperty("approved");
	}
	
	public String getIcon(){
		if (this.getApproved()){
			return "fa-check";
		} else {
			return "fa-lightbulb-o";
		}
	}
	
	public String getColorClass(){
		if (this.getApproved()){
			return "box-success";
		} else {
			return "box-warning";
		}
	}
	
	public String getParentUuid(){
		return (String) this.getEntity().getProperty("parentUuid");
	}
	
	public String getApprovedSolutionUrl(){
		return "/mark-approved-answer/" + this.getEntity().getProperty("uuid");
	}
	
	public String getNotApprovedSolutionUrl(){
		return "/mark-approved-answer/not/" + this.getEntity().getProperty("uuid");
	}

	public void markApproved() {
		this.getEntity().setProperty("approved", true);
		datastore.put(this.getEntity());
	}
	
	public void markNotApproved() {
		this.getEntity().setProperty("approved", false);
		datastore.put(this.getEntity());
	}
	
}

