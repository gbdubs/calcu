package calculus.api;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import calculus.models.Answer;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ContentAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static Filter submittedFilter = new FilterPredicate("submitted", FilterOperator.EQUAL, true);
	private static Filter viewableFilter = new FilterPredicate("viewable", FilterOperator.EQUAL, true);
	private static Filter compositeFilter = CompositeFilterOperator.and(viewableFilter, submittedFilter);

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
			Content c = instantiateContent(contentUuid);
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
			content.add(instantiateContent(e));
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
				content.add(instantiateContent(future.get()));
			} catch (InterruptedException | ExecutionException e) {
				// Don't include in results if interrupted.
			}
		}
		return content;
	}

	public static Set<Content> getContentSampling(int n, int seed, String userId) {
		Query newQ = new Query("Content").addSort("createdAt", SortDirection.DESCENDING).setFilter(compositeFilter);
		PreparedQuery newPQ = datastore.prepare(newQ);
		List<Entity> newE = newPQ.asList(FetchOptions.Builder.withLimit(n/4));
		
		Query bestQ = new Query("Content").addSort("karma", SortDirection.DESCENDING).setFilter(compositeFilter);
		PreparedQuery bestPQ = datastore.prepare(bestQ);
		List<Entity> bestE = bestPQ.asList(FetchOptions.Builder.withLimit(n/4));
		
		Query allQ = new Query("Content").setFilter(compositeFilter);
		PreparedQuery allPQ = datastore.prepare(allQ);
		List<Entity> allE = allPQ.asList(FetchOptions.Builder.withLimit(n/4).offset((int) (Math.random() * 1000)));
		
		List<Content> randomResults = RandomValuesAPI.randomContents(n/4);
		
		Set<Content> result = new HashSet<Content>();

		for(Entity e : newE){
			result.add(instantiateContent(e));
		}
		for(Entity e : bestE){
			result.add(instantiateContent(e));
		}
		for(Entity e : allE){
			result.add(instantiateContent(e));
		}
		result.addAll(randomResults);
		
		return result;
	}

	private static Content instantiateContent(String uuid) throws EntityNotFoundException{
		Entity e = datastore.get(KeyFactory.createKey("Content", uuid));
		return instantiateContent(e);
	}
	
	private static Content instantiateContent(Entity e) {
		String contentType = (String) e.getProperty("contentType");
		if (contentType.equals("quesiton")){
			return new Question(e);
		} else if (contentType.equals("practiceProblem")){
			return new PracticeProblem(e);
		} else if (contentType.equals("answer")){
			return new Answer(e);
		} else if (contentType.equals("textContent")){
			return new TextContent(e);
		}
		return null;
	}

	public static String getContentAsJson(List<Content> content) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(content);
		return json;
	}
}
