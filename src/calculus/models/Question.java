package calculus.models;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Question extends Content{
	
	private static List<String> FIELDS = new ArrayList<String>();
	
	static {
		FIELDS.addAll(Content.FIELDS);
		FIELDS.add("answers");
	}
	
	public Question(String uuid) {
		super(uuid, "question");
	}
	
	public Question(Key key) {
		super(key, "question");
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

}
