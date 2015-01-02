package calculus.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Question extends Content{
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static UserService userService = UserServiceFactory.getUserService();
	
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
	
	public static Question createQuestionFromRequest(HttpServletRequest req){
		
		String uuid = UUID.randomUUID().toString();
		String parentUuid = req.getParameter("parentUuid");
		long time = System.currentTimeMillis();
		
		boolean anonymous = false;
		boolean submitted = (req.getParameter("saveWork") == null);
		boolean viewable = (req.getParameter("saveWork") == null);
		
		String title = (String) req.getParameter("title");
		if (title == null || title == "") title = "[Un-named Question]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[This author was so caught up in the existential crisis of life, they realized that the only true question is 'why?', and can best be represented by not putting in anything in their question's body]";
				
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setProperty("uuid", uuid);
		entity.setProperty("parentUuid", parentUuid);
		entity.setProperty("contentType", "question");
		entity.setProperty("creatorUserId", UserServiceFactory.getUserService().getCurrentUser().getUserId());
		entity.setProperty("createdAt", time);
		entity.setProperty("title", title);
		entity.setProperty("body", body);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", "/question/" + uuid);
		
		datastore.put(entity);
		
		return new Question(entity);
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
