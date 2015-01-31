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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

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
	
	public Answer(Key key) {
		super(key, "answer");
	}
	
	public Answer(Entity entity) {
		super(entity, "answer");
	}
	
	public static Answer createAnswerFromRequest(HttpServletRequest req){
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String userId = "";
		if (user != null) userId = user.getUserId();
		
		String uuid = UUID.randomUUID().toString();
		String parentUuid = req.getParameter("parentUuid");
		long time = System.currentTimeMillis();
		
		boolean anonymous = (req.getParameter("anonymousSubmit") != null);
		boolean submitted = true;
		boolean viewable = true;
		
		String title = (String) req.getParameter("title");
		if (title == null || title == "") title = "[Un-named Answer]";
		String body = (String) req.getParameter("body");
		if (body == null || body == "") body = "[The Author has opted to leave the answer blank, becuase they think it is self evident]";
		Text wrappedBody = new Text(body);		
		
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		entity.setProperty("uuid", uuid);
		entity.setProperty("karma", 0);
		entity.setProperty("parentUuid", parentUuid);
		entity.setProperty("contentType", "answer");
		entity.setProperty("creatorUserId", userId);
		entity.setProperty("createdAt", time);
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("approved", false);
		entity.setProperty("url", "/content/"+parentUuid);
		
		datastore.put(entity);
		
		return new Answer(entity);
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

