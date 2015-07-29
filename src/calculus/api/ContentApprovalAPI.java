package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.Content;
import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class ContentApprovalAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static Entity getContentApprovalEntity(){
		try {
			Entity e = datastore.get(KeyFactory.createKey("ContentApprovalEntity", "OneAndOnly"));
			return e;
		} catch (EntityNotFoundException error) {
			Entity e = new Entity(KeyFactory.createKey("ContentApprovalEntity", "OneAndOnly"));
			List<Content> contentList = ContentAPI.getAllContentOfType("question");
			contentList.addAll(ContentAPI.getAllContentOfType("practiceProblem"));
			contentList.addAll(ContentAPI.getAllContentOfType("textContent"));
			List<String> contentUuids = new ArrayList<String>();
			for(Content c : contentList){
				contentUuids.add(c.getUuid());
			}
			e.setUnindexedProperty("toApprove", contentUuids);
			return e;
		}
	}
	
	public static Content getPieceOfContentToApprove(){
		Entity e = getContentApprovalEntity();
		List<String> toApprove = SafeList.string(e, "toApprove");
		if (toApprove.size() == 0){
			return null;
		}
		String uuid = toApprove.remove(toApprove.size() - 1);
		e.setUnindexedProperty("toApprove", toApprove);
		datastore.put(e);
		try {
			return ContentAPI.instantiateContent(uuid);
		} catch (EntityNotFoundException e1) {
			return null;
		}
	}
	
	public static void markContentApproved(String uuid){
		Entity e = getContentApprovalEntity();
		List<String> approved = SafeList.string(e, "approved");
		approved.add(uuid);
		e.setUnindexedProperty("approved", approved);
		datastore.put(e);
	}
	
	public static void markContentNotApproved(String uuid){
		Entity e = getContentApprovalEntity();
		List<String> notApproved = SafeList.string(e, "notApproved");
		notApproved.add(uuid);
		e.setUnindexedProperty("notApproved", notApproved);
		datastore.put(e);
	}
	
}
