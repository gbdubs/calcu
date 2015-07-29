package calculus.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import calculus.models.Content;
import calculus.models.Topic;
import calculus.utilities.CountMap;
import calculus.utilities.SafeList;
import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

public class TopicAPI {

	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static Map<String, String> topicMapping = new HashMap<String, String>();
	
	public static Topic getTopicWithAproximateDifficulty(int difficulty){
		
		// TODO
		
		
		return null;
	}
	
	public static Topic getOrCreateTopicFromUrl(String topicUrl){
		if (topicUrl.equals("")){
			return getOrCreateTopicFromUrl("NONE");
		}
		topicUrl = topicUrl.toLowerCase();
		Entity titleMappingEntity = Topic.getTopicTitleMapping();
		
		String title = topicUrl.substring(topicUrl.lastIndexOf('/') + 1).toLowerCase();
		
		if (titleMappingEntity.getProperty(topicUrl) != null){
			return getTopicByUUID((String) titleMappingEntity.getProperty(topicUrl));
		}
		
		if (topicMapping.get(topicUrl) != null){
			return getTopicByUUID(topicMapping.get(topicUrl));
		}
		
		System.out.println("CREATING FROM URL: " + topicUrl + " TITLE: " + title);
		Topic result = getTopicByUUID(createTopicFromUrl(topicUrl));
		
		titleMappingEntity.setUnindexedProperty(topicUrl, result.getUuid());
		topicMapping.put(topicUrl, result.getUuid());
		datastore.put(titleMappingEntity);
		
		return result;
	}
	
	private static Topic getTopicByUUID(String uuid){
		if (UuidTools.getUuidFromUrl(uuid) == null){
			return null;
		}
		try {
			return new Topic(uuid);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}
	
	private static String createTopicFromUrl(String topicUrl){
		String[] topicTitles = topicUrl.split("/");
		
		if (topicTitles.length >= 1){
			Topic t = Topic.createNewTopic(topicTitles[topicTitles.length - 1].toLowerCase(), "AUTO GENERATED", "AUTO GENERATED", "AUTO GENERATED");
			
			if (topicTitles.length > 1){
				String parental = topicTitles[0];
				for (int i = 1; i < topicTitles.length - 1; i++){
					parental += "/" + topicTitles[i];
				}
				Topic parent = getOrCreateTopicFromUrl(parental.toLowerCase());
				parent.addSubTopic(t.getUuid());
				t.addParentTopic(parent.getUuid());
				parent.save();
			}
			Entity titleMapping = Topic.getTopicTitleMapping();
			titleMapping.setUnindexedProperty(topicUrl.toLowerCase(), t.getUuid());
			datastore.put(titleMapping);
			topicMapping.put(topicUrl.toLowerCase(), t.getUuid());
			t.save();
			return t.getUuid();
		}
		return null;
	}
	
	public static boolean updateTopicTitle(String uuid, String newTitle){
		Topic t = getTopicByUUID(uuid);
		if (t != null){
			t.setTitle(newTitle);
			return true;
		}
		return false;
	}
	
	public static boolean updateTopicShortDescription(String uuid, String newDescription){
		Topic t = getTopicByUUID(uuid);
		if (t != null){
			t.setShortDescription(newDescription);
			t.save();
			return true;
		}
		return false;
	}
	
	public static boolean updateTopicLongDescription(String uuid, String newDescription){
		Topic t = getTopicByUUID(uuid);
		if (t != null){
			t.setLongDescription(newDescription);
			t.save();
			return true;
		}
		return false;
	}
	
	public static boolean updateTopicTags(String uuid, String newTags){
		Topic t = getTopicByUUID(uuid);
		if (t != null){
			t.setTags(newTags);
			t.save();
			return true;
		}
		return false;
	}
	
	public static boolean updateTopicDifficulty(String uuid, int newDifficulty){
		Topic t = getTopicByUUID(uuid);
		if (t != null){
			t.setDifficulty(newDifficulty);
			t.save();
			return true;
		}
		return false;
	}
	
	public static void assignTopicDifficulty(Topic t, int difficulty){
		t.setDifficulty(difficulty);
		t.save();
	}

	public static void makeTopicAParent(Topic parent, Topic child){
		parent.addSubTopic(child.getUuid());
		child.addParentTopic(parent.getUuid());
		parent.save();
		child.save();
	}

	public static String getAllTopicsData() {
		Entity e = getAllTopicsEntity();
		return ((Text) e.getProperty("allData")).getValue();
	}

	public static List<Topic> getRootTopics(){
		Entity e = getAllTopicsEntity();
		return TopicAPI.getTopicsAsync(SafeList.string(e, "rootTopicUuids"));
	}

	private static Entity getAllTopicsEntity(){
		try {
			Entity e = datastore.get(KeyFactory.createKey("TopicTracker", "OneAndOnly"));
			if (System.currentTimeMillis() - (Long) e.getProperty("updatedAt") < 24 * 60 * 60 * 1000){
				return e;
			}
		} catch (EntityNotFoundException enfe) {
			
		}
		Entity e = new Entity(KeyFactory.createKey("TopicTracker", "OneAndOnly"));
		e.setUnindexedProperty("updatedAt", System.currentTimeMillis());
		
		List<Topic> allTopics = bruteForceGetTopics();
		StringBuffer data = new StringBuffer();
		for (Topic t : allTopics){
			data.append("<div id=\"ts-"+t.getUuid()+"-data\">");
			data.append("<div class=\"title\">"+t.getTitle()+"</div>");
			data.append("<div class=\"sub-topics\">"+t.getSubTopics().toString()+"</div>");
			data.append("<div class=\"content-size\">"+t.getContentUuids().size()+"</div>");
			data.append("</div>");
		}
		e.setUnindexedProperty("allData", new Text(data.toString()));
		
		List<Topic> rootTopics = bruteForceGetRootTopics();
		List<String> rootTopicUuids = new ArrayList<String>();
		for (Topic t : rootTopics){
			rootTopicUuids.add(t.getUuid());
		}
		e.setUnindexedProperty("rootTopicUuids", rootTopicUuids);
		datastore.put(e);
		return e;
	}
	
	private static List<Topic> bruteForceGetTopics(){
		List<Topic> topics = new ArrayList<Topic>();
		Query q = new Query("Topic");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			Topic t = new Topic(e);
			if (t != null){
				topics.add(t);
			}
		}
		return topics;
	}
	
	public static List<Topic> downloadAllTopics(){
		return bruteForceGetRootTopics();
	}
	
	private static List<Topic> bruteForceGetRootTopics(){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Topic> topics = new ArrayList<Topic>();
		Query q = new Query("Topic");
		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()){
			Topic t = new Topic(e);
			if (t != null && t.getParentTopics().size() == 0){
				topics.add(t);
			}
		}
		return topics;
	}

	public static void recalculateTags(String topicUuid, int n){
		CountMap<String> countMap = new CountMap<String>();
		
		Topic t = TopicAPI.getTopicByUUID(topicUuid);
		
		if (t == null){
			return;
		}
		
		List<String> contentUuids = t.getContentUuids();
		List<Content> allContent = ContentAPI.getContentAsync(contentUuids);
		
		for (Content c : allContent){
			for(String tag : c.getListOfTags()){
				countMap.increment(tag);
			}
		}
		
		List<String> sortedTags = countMap.getDescendingList(n);
		
		String result = "";
		for (String tag : sortedTags){
			result += ", " + tag;
		}
		if (result.length() > 0){
			t.setTags(result.substring(2));
			t.saveAsync();
		}
	}
	
	public static void recalculateAllTopicTags(){
		List<Topic> allTopics = bruteForceGetTopics();
		for (Topic t : allTopics){
			recalculateTags(t.getUuid(), 10);
		}
	}
	
	public static List<Topic> getTopicsAsync(List<String> uuids){
		List<Topic> topics = new ArrayList<Topic>();
		List<Future<Entity>> allFutures = new ArrayList<Future<Entity>>();
		for (String uuid : uuids){
			allFutures.add(asyncDatastore.get(KeyFactory.createKey("Topic", uuid)));
		}
		for (Future<Entity> f : allFutures){
			Entity e;
			try {
				e = f.get();
				Topic t = new Topic(e);
				topics.add(t);
			} catch (InterruptedException e1) {
				
			} catch (ExecutionException e1) {
	
			}
		}
		return topics;
	}
}

