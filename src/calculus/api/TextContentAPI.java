package calculus.api;

import java.util.ArrayList;
import java.util.List;

import calculus.models.Content;
import calculus.models.TextContent;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class TextContentAPI {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static TextContent[] getTextContentCalibrationForUser(String userId) {
		TextContent[] result = new TextContent[2];
		Filter contentFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, "textContent");
		Query q = new Query("Content").setFilter(contentFilter).addSort("createdAt");
		PreparedQuery pq = datastore.prepare(q);
		int i = 0;
		for (Entity entity : pq.asIterable()){
			result[i++] = new TextContent(entity);
			if (i >= 2) return result;
		}
		return result;
	}

	public static List<TextContent> getAllTextContent() {
		List<Content> content = ContentAPI.getAllContentOfType("answer");
		List<TextContent> tcs = new ArrayList<TextContent>();
		for(Content c : content){
			if (c.getContentType().equals("textContent")){
				TextContent tc = (TextContent) c;
				tcs.add(tc);
			}
		}
		return tcs;
	}
}
