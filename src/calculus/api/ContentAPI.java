package calculus.api;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import calculus.models.Content;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ContentAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	private static Filter compositeFilter = CompositeFilterOperator.and(viewableFilter, submittedFilter);
	
	public static List<Content> getNewContent(int i) {
		Query q = new Query("Content").addSort("createdAt", SortDirection.DESCENDING).setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i));
		List<Content> results = new ArrayList<Content>();
		for (Entity e : entities){
			results.add(new Content(e));
		}
		return results;
	}
	
	public static List<Content> getBestContent(int i) {
		Query q = new Query("Content").addSort("karma", SortDirection.DESCENDING).setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i));
		List<Content> results = new ArrayList<Content>();
		for (Entity e : entities){
			results.add(new Content(e));
		}
		return results;
	}
	
	public static List<Content> getSuggestedContent(int i, String userId) {
		return getNewContent(i);
	}
	
	//TODO: This
	public static List<Content> getRandomContent(int i) {
		Query q = new Query("Content").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(i * 2).offset((int) (Math.random() * 1000)));
		List<Content> results = new ArrayList<Content>();
		while (results.size() < i){
			int k = (int) (Math.random() * entities.size());
			Entity e = entities.remove(k);
			results.add(new Content(e));
		}
		return results;
	}

	public static String getContentType(String uuid) throws EntityNotFoundException{
		if (uuid == "" || uuid == null) return null;
		Key contentKey = KeyFactory.createKey("Content", uuid);
		Entity entity = datastore.get(contentKey);
		return (String) entity.getProperty("contentType");
	}

	public static void setInvisible(String uuid){
		Key contentKey = KeyFactory.createKey("Content", uuid);
		Entity entity;
		try {
			entity = datastore.get(contentKey);
			entity.setProperty("viewable", false);
			datastore.put(entity);
		} catch (EntityNotFoundException e) {
			// Don't worry if it doesn't exist, we need not make it less visible!
		}
	}

	public static String getBoxColor(String contentType){
		if (contentType.equals("question")){
			return "info";
		} else if (contentType.equals("answer")){
			return "danger";
		} else if (contentType.equals("textContent")){
			return "warning";
		} else if (contentType.equals("practiceProblem")){
			return "success";
		} else {
			return "default";
		}
	}

	public static String getBoxIcon(String contentType){
		if (contentType.equals("question")){
			return "fa-question";
		} else if (contentType.equals("answer")){
			return "fa-lightbulb-o";
		} else if (contentType.equals("textContent")){
			return "fa-newspaper-o";
		} else if (contentType.equals("practiceProblem")){
			return "fa-pencil";
		} else {
			return "fa-line-chart";
		}
	}

	public static void setVisible(String uuid) {
		Key contentKey = KeyFactory.createKey("Content", uuid);
		Entity entity;
		try {
			entity = datastore.get(contentKey);
			entity.setProperty("viewable", true);
			datastore.put(entity);
		} catch (EntityNotFoundException e) {
			// Don't worry if it doesn't exist, we need not make it less visible!
		}
	}

	public static String getContentAuthorId(String contentUuid) {
		try {
			Content c = new Content(contentUuid);
			return c.getAuthor().getUserId();
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public static List<Content> getContentWithAuthor(String userId, int maxToDisplay, int offset) {
		Filter userFilter = new FilterPredicate("creatorUserId", FilterOperator.EQUAL, userId);
		Filter totalFilter = CompositeFilterOperator.and(userFilter, compositeFilter);
		Query q = new Query("Content").addSort("createdAt", SortDirection.DESCENDING).setFilter(totalFilter);
		
		List<Entity> queryResults = datastore.prepare(q).asList(withLimit(maxToDisplay).offset(offset));
		List<Content> content = new ArrayList<Content>();
		for (Entity e : queryResults){
			content.add(new Content(e));
		}
		return content;
	}

	public static String randomColor() {
		double rand = Math.random();
		if (rand < .2){
			return "danger";
		} else if (rand < .4){
			return "warning";
		} else if (rand < .6){
			return "success";
		} else if (rand < .8){
			return "primary";
		} else {
			return "info";
		}
	}

	public static void addAnswerToContent(String parentUuid, String answerUuid) {
		String contentType = "";
		try {
			contentType = getContentType(parentUuid);
		} catch (EntityNotFoundException e) {
			// Don't worry if it doesn't exist!
		}
		if (contentType.equals("practiceProblem")){
			PracticeProblemAPI.addAnswerToPracticeProblem(parentUuid, answerUuid);
		} else if (contentType.equals("question")){
			QuestionAPI.addAnswerToQuestion(parentUuid, answerUuid);
		} else if (contentType.equals("textContent")){
			TextContentAPI.addAnswerToTextContent(parentUuid, answerUuid);
		} else {
			System.out.println("You have not done this correctly");
		}
	}

	private static Future<Entity> futureContent(String uuid) {
		Future<Entity> future = asyncDatastore.get(KeyFactory.createKey("Content", uuid));
		return future;
	}
	
	public static List<Content> getContentAsync(List<String> uuids){
		List<Future<Entity>> futures = new ArrayList<Future<Entity>>();
		for(String uuid : uuids){
			futures.add(futureContent(uuid));
		}
		List<Content> content = new ArrayList<Content>();
		for(Future<Entity> future : futures){
			try {
				content.add(new Content(future.get()));
			} catch (InterruptedException | ExecutionException e) {
				// Don't include in results if interrupted.
			}
		}
		return content;
	}

}
