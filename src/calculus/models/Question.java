package calculus.models;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Question extends Content{
	
	private static List<String> FIELDS = new ArrayList<String>();
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	
	static {
		FIELDS.addAll(Content.FIELDS);
		FIELDS.add("answers");
	}
	
	public Question(String uuid) {
		super(uuid, "question");
	}
	
	public Question(Entity entity) {
		super(entity, "question");
	}
	
	@Override
	public void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property Modification.");
	}

	public String getEditUrl(){
		return "/contribute/question/edit/" + this.getUuid();
	}
}
