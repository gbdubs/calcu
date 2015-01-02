package calculus.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Answer extends Content {

	static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	static UserService userService = UserServiceFactory.getUserService();
	
	private static List<String> FIELDS = new ArrayList<String>();
	
	static {
		FIELDS.addAll(Content.FIELDS);
		FIELDS.add("parentUuid");
	}
	
	public Answer(String uuid) {
		super(uuid, "answer");
	}
	
	public Answer(Key key) {
		super(key, "answer");
	}
	
	public Answer(Entity entity) {
		super(entity, "answer");
	}

	
	
	
	public static Answer createAnswerFromRequest(HttpServletRequest req){
		
		String uuid = UUID.randomUUID().toString();
		String parentUuid = req.getParameter("parentUuid");
		
		
		
		
		return null;
	}
	
	@Override
	public void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property modification.");
	}

}
