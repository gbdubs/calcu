package calculus.api;

import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import calculus.contribute.ContributeAnswerServlet;
import calculus.models.Answer;
import calculus.models.Content;
import calculus.models.Notification;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class AnswersAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static boolean userHasAnsweredContent(String userId, String uuid) {
		Filter answerFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "answer");
		Filter problemFilter = new FilterPredicate("parentUuid", FilterOperator.EQUAL, uuid);
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter compositeFilter = CompositeFilterOperator.and(problemFilter, userFilter, answerFilter);
		Query query = new Query("Content").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(query);
		for (Iterator<Entity> iterator = pq.asIterable().iterator(); iterator.hasNext();) {
			return true;
		}
		return false;
	}

	public static Answer createAnswerFromRequest(HttpServletRequest req){
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String userId = "";
		if (user != null) userId = user.getUserId();
		
		String uuid = UUID.randomUUID().toString();
		String parentUuid = req.getParameter("parentUuid");
		String parentUrl = "";
		try {
			parentUrl = (new Content(parentUuid)).getUrl();
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Orphaned Answer attempeted to be created.");
		}
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
		
		entity.setUnindexedProperty("uuid", uuid);
		entity.setProperty("karma", 0);
		entity.setProperty("parentUuid", parentUuid);
		entity.setProperty("contentType", "answer");
		entity.setProperty("creatorUserId", userId);
		entity.setProperty("createdAt", time);
		entity.setUnindexedProperty("title", title);
		entity.setUnindexedProperty("body", wrappedBody);
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("approved", false);
		entity.setUnindexedProperty("url", parentUrl);
		
		datastore.put(entity);
		
		return new Answer(entity);
	}

}
