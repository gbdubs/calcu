package calculus.recommendation;

import java.util.List;

import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class HelpfulContentAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static void userFoundContentHelpful(String userId, String contentUuid){
		Entity entity = getHelpfulContentEntity(userId);
		List<String> helpfulContent = getHelpfulContent(entity);
		if (!helpfulContent.contains(contentUuid)){
			helpfulContent.add(contentUuid);
			setHelpfulContent(entity, helpfulContent);
			datastore.put(entity);
		}
	}

	protected static List<String> getHelpfulContent(String userId) {
		return getHelpfulContent(getHelpfulContentEntity(userId));	
	}

	private static Entity getHelpfulContentEntity(String userId){
		Key key = KeyFactory.createKey("HelpfulContent", userId);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			return new Entity(key);
		}
	}
	
	private static List<String> getHelpfulContent(Entity e){
		return SafeList.string(e, "helpfulContent");
	}
	
	private static void setHelpfulContent(Entity e, List<String> l){
		e.setUnindexedProperty("helpfulContent", l);
	}
}
