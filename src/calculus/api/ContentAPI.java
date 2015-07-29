package calculus.api;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import calculus.models.Answer;
import calculus.models.Author;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.utilities.Cleaner;
import calculus.utilities.UuidTools;

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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
			Author a = c.getAuthor();
			if (a == null){
				return null;
			}
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

	public static void addAnswerToContent(String contentUuid, String answerUuid){
		Content c;
		try {
			c = ContentAPI.instantiateContent(contentUuid);
			c.addAnswer(answerUuid);
		} catch (EntityNotFoundException e) {
			// We cannot update a non-existent problem.
		}
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
		
		List<Content> randomResults = RandomValuesAPI.randomContents(n/2);
		
		Set<Content> result = new HashSet<Content>();

		for(Entity e : newE){
			result.add(instantiateContent(e));
		}
		for(Entity e : bestE){
			result.add(instantiateContent(e));
		}
		result.addAll(randomResults);
		
		return result;
	}

	public static Content instantiateContent(String uuid) throws EntityNotFoundException{
		Entity e = datastore.get(KeyFactory.createKey("Content", uuid));
		return instantiateContent(e);
	}
	
	public static Content instantiateContent(Entity e) {
		String contentType = (String) e.getProperty("contentType");
		if (contentType.equals("question")){
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

	public static Content constructContentFromJson(JsonObject content){
		String contentType = content.get("contentType").getAsString();
		String uuid = extractStringFromJsonObjectWithDefault(content.get("uuid"), UUID.randomUUID().toString());
		String creatorUserId = extractStringFromJsonObjectWithDefault(content.get("creatorUserId"), "administrator");
		long createdAt = extractLongFromJsonObjectWithDefault(content.get("createdAt"), System.currentTimeMillis());
		String title = extractStringFromJsonObjectWithDefault(content.get("title"), "[Untitled " + contentType + "]");
		String body = extractStringFromJsonObjectWithDefault(content.get("body"), "[No " + contentType + " Body]");
		boolean anonymous = extractBooleanFromJsonObjectWithDefault(content.get("anonymous"), true);
		boolean submitted = extractBooleanFromJsonObjectWithDefault(content.get("submitted"), true);
		boolean viewable = extractBooleanFromJsonObjectWithDefault(content.get("viewable"), true);
		String url = extractStringFromJsonObjectWithDefault(content.get("url"), "/content/" + uuid);
		int karma = extractIntFromJsonObjectWithDefault(content.get("karma"), 1);
		String tags = extractStringFromJsonObjectWithDefault(content.get("tags"), "");
		String source = extractStringFromJsonObjectWithDefault(content.get("source"), "[No-Source-from-Json]");
		List<String> allAnswers = extractListOfStringsFromJsonObjectWithDefault(content.get("allAnswers"), new ArrayList<String>());
		
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		entity.setProperty("contentType", contentType);
		entity.setProperty("uuid", uuid);
		entity.setProperty("creatorUserId", creatorUserId);
		entity.setProperty("createdAt", createdAt);
		entity.setProperty("title", title);
		entity.setProperty("body", new Text(body));
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("url", url);
		entity.setProperty("karma", new Long(karma));
		entity.setProperty("tags", tags);
		entity.setProperty("allAnswers", allAnswers);
		entity.setProperty("previousTags", "");
		entity.setProperty("source", source);
		
		Content result = null;
		
		if (contentType.equals("answer")){
			String parentUuid = extractStringFromJsonObjectWithDefault(content.get("parentUuid"), null);
			entity.setProperty("parentUuid", parentUuid);
			boolean approved = extractBooleanFromJsonObjectWithDefault(content.get("approved"), false);
			entity.setProperty("approved", approved);
			entity.setProperty("url", "/content/" + parentUuid);
			
			result = new Answer(entity);
			
		} else if (contentType.equals("practiceProblem")){
			String authorSolution =  extractStringFromJsonObjectWithDefault(content.get("authorSolution"), "[No Author Solution]");
			Text wrappedSolution = new Text(authorSolution);
			entity.setUnindexedProperty("authorSolution", wrappedSolution);
			result = new PracticeProblem(entity);
			
		} else if (contentType.equals("question")){
			result = new Question(entity);
			
		} else if (contentType.equals("textContent")){
			result = new TextContent(entity);
		}
		
		return result;
	}

	public static String createOrUpdateContentFromRequest(HttpServletRequest req, String contentType){
		
		String uuid = UuidTools.getUuidFromUrl(req.getParameter("uuid"));
		if (uuid == null){
			uuid = UUID.randomUUID().toString();
		}
		
		Entity entity = new Entity(KeyFactory.createKey("Content", uuid));
		
		boolean anonymous = (req.getParameter("saveButton").equals("Submit Anonymously"));
		boolean submitted = (req.getParameter("saveButton").equals("Submit") || anonymous);
		boolean viewable = submitted;
		
		String title = (String) req.getParameter("title");
		title = Cleaner.autoclave(title);
		if (title == null || title.equals("")) title = "[Un-named "+contentType+"]";
		
		String body = (String) req.getParameter("body");
		body = Cleaner.cleanHtml(body);
		if (body == null || body.equals("")) body = "[No "+contentType+" Body]";
		Text wrappedBody = new Text(body);
	
		String tags = req.getParameter("tagsInput");
		if (tags == null) tags = "";
		tags = Cleaner.cleanHtml(tags);
		
		String url = "/content/" + uuid;
		if (contentType.equals("answer")){
			String parentUuid = req.getParameter("parentUuid");
			try {
				url = (ContentAPI.instantiateContent(parentUuid)).getUrl();
			} catch (EntityNotFoundException e) {
				throw new RuntimeException("Orphaned Answer attempeted to be created.");
			}
			entity.setProperty("parentUuid", parentUuid);
			entity.setProperty("approved", false);
		} else if (contentType.equals("practiceProblem")){
			String authorSolution = (String) req.getParameter("authorSolution");
			authorSolution = Cleaner.cleanHtml(authorSolution);
			if (authorSolution == null || authorSolution.equals("")) authorSolution = "[The Author has not provided an answer to this problem]";
			Text wrappedAuthorSolution = new Text(authorSolution);
			entity.setProperty("authorSolution", wrappedAuthorSolution);
		}
		
		String creatorId = "";
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user != null){
			creatorId = user.getUserId();	
		}
		
		// Here, we can set the entity properties to all be indexed, because we are only saving through the 
		// Content Model.  We only want that SINGLE class to determine which properties are indexed.
		entity.setProperty("uuid", uuid);
		entity.setProperty("contentType", contentType);
		entity.setProperty("creatorUserId", creatorId);
		entity.setProperty("createdAt", System.currentTimeMillis());
		entity.setProperty("anonymous", anonymous);
		entity.setProperty("submitted", submitted);
		entity.setProperty("viewable", viewable);
		entity.setProperty("karma", new Long(1));
		entity.setProperty("title", title);
		entity.setProperty("body", wrappedBody);
		entity.setProperty("allAnswers", new ArrayList<String>());
		entity.setProperty("tags", tags);
		entity.setProperty("url", url);
		entity.setProperty("source", "user");
		
		Content c = ContentAPI.instantiateContent(entity);
		
		c.saveAsync();
		
		return uuid;
	}

	public static List<Content> getAllContentOfType(String contentType){
		List<Content> results = new ArrayList<Content>();
		Query q = new Query("Content");
		Filter typeFilter = new FilterPredicate("contentType", FilterOperator.EQUAL, contentType);
		q.addSort("createdAt").setFilter(typeFilter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			Content c = ContentAPI.instantiateContent(e);
			results.add(c);
		}
		return results;
	}
	
	private static List<String> extractListOfStringsFromJsonObjectWithDefault(JsonElement jsonElement, List<String> defaultList){
		if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonArray()){
			List<String> allAnswers = new ArrayList<String>();
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement element: jsonArray){
				allAnswers.add(element.getAsString());
			}
		}
		return defaultList;
	}

	private static boolean extractBooleanFromJsonObjectWithDefault(JsonElement jsonElement, boolean defaultBool) {
		if (jsonElement == null || jsonElement.isJsonNull()){
			return defaultBool;
		}
		return jsonElement.getAsBoolean();
	}

	private static int extractIntFromJsonObjectWithDefault(JsonElement jsonElement, int defaultInt) {
		if (jsonElement == null || jsonElement.isJsonNull()){
			return defaultInt;
		}
		return jsonElement.getAsInt();
	}

	private static long extractLongFromJsonObjectWithDefault(JsonElement jsonElement, long defaultLong) {
		if (jsonElement == null || jsonElement.isJsonNull()){
			return defaultLong;
		}
		return jsonElement.getAsLong();
	}

	private static String extractStringFromJsonObjectWithDefault(JsonElement je, String defaultString){
		if (je == null || je.isJsonNull()){
			return defaultString;
		}
		return je.getAsString();
	}
}
