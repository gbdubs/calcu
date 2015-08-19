package calculus.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


public class TopicAPI {

	private static final int topicSelectorUpdateInterval = 24 * 60 * 60 * 1000;
	public static final int MAX_TOPIC_DIFFICULTY = 750000;
	
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static Map<String, String> topicMapping = new HashMap<String, String>();
	
	public static Topic getTopicWithAproximateDifficulty(int difficulty){
		difficulty = Math.max(difficulty, 500);
		difficulty = Math.min(difficulty, MAX_TOPIC_DIFFICULTY - 500);
		
		Filter lowerBound = new FilterPredicate("difficulty", FilterOperator.GREATER_THAN, difficulty - 501);
		Filter upperBound = new FilterPredicate("difficulty", FilterOperator.GREATER_THAN, difficulty + 501);
		Filter compositeFilter = CompositeFilterOperator.and(lowerBound, upperBound);
		Query q = new Query("Topic").setFilter(compositeFilter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			return new Topic(e);
		}
		return null;
	}
	
	public static Topic getOrCreateTopicFromUrl(String topicUrl){
		if (topicUrl.equals("")){
			return getOrCreateTopicFromUrl("NONE");
		}
		topicUrl = topicUrl.toLowerCase();
		Entity titleMappingEntity = Topic.getTopicTitleMapping();
		
		if (titleMappingEntity.getProperty(topicUrl) != null){
			return getTopicByUUID((String) titleMappingEntity.getProperty(topicUrl));
		}
		
		if (topicMapping.get(topicUrl) != null){
			return getTopicByUUID(topicMapping.get(topicUrl));
		}
		
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
			if (System.currentTimeMillis() - (Long) e.getProperty("updatedAt") < topicSelectorUpdateInterval){
				return e;
			}
		} catch (EntityNotFoundException enfe) {
			orderAllSubTopics();
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
	
	private static void orderAllSubTopics(){
		List<Topic> allTopics = bruteForceGetTopics();
		Map<String, Topic> lookup = new HashMap<String, Topic>();
		for (Topic t : allTopics){
			lookup.put(t.getUuid(), t);
		}
		for (Topic t : allTopics){
			List<String> subTopics = t.getSubTopics();
			if (!subTopics.isEmpty()){
				List<Topic> subs = new ArrayList<Topic>();
				for (String s : subTopics){
					subs.add(lookup.get(s));
				}
				subTopics.clear();
				Collections.sort(subs, new StrangeComparator());
				for (Topic sub : subs){
					subTopics.add(sub.getUuid());
				}
				t.setSubTopics(subTopics);
				t.save();
			}
		}
	}
	
	private static class StrangeComparator implements Comparator<Topic>{

		@Override
		public int compare(Topic t1, Topic t2) {
			int d1 = t1.getDifficulty();
			int d2 = t2.getDifficulty();
			if (d1 < 0 && d2 < 0){
				return 0;
			} else if (d1 < 0){
				return 1;
			} else if (d2 < 0){
				return -1;
			} else {
				return Integer.compare(d1, d2);
			}
		}
		
	}
	
	private static void setTopicTrackerToUpdateNextTime(){
		try {
			Entity e = datastore.get(KeyFactory.createKey("TopicTracker", "OneAndOnly"));
			e.setUnindexedProperty("updatedAt", System.currentTimeMillis() - topicSelectorUpdateInterval);
		} catch (EntityNotFoundException e) {
			return;
		}
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
		Query q = new Query("Topic").addSort("difficulty");
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
	
	
	public static boolean mergeTopicIntoTopic(String tUuid1, String tUuid2){
		Topic t1;
		try {
			t1 = new Topic(tUuid1);
			Topic t2 = new Topic(tUuid2);
			return mergeTopicIntoTopic(t1, t2);	
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

	
	public static boolean mergeTopicIntoTopic(Topic t1, Topic t2){
		for (String contentUuid : t1.getContentUuids()){
			t2.addContentUuid(contentUuid);
			try {
				Content c = ContentAPI.instantiateContent(contentUuid);
				c.setTopicUuid(t2.getUuid());
				c.saveAsync();
			} catch (EntityNotFoundException e) {
				return false;
			}
		}
		for (String subTopicUuid : t1.getSubTopics()){
			t2.addSubTopic(subTopicUuid);
			try {
				Topic transferredChild = new Topic(subTopicUuid);
				transferredChild.removeParentTopic(t1.getUuid());
				transferredChild.addParentTopic(t2.getUuid());
				transferredChild.saveAsync();
			} catch (EntityNotFoundException e) {
				return false;
			}
		}
		t2.save();
		datastore.delete(KeyFactory.createKey("Topic", t1.getUuid()));
		setTopicTrackerToUpdateNextTime();
		return true;
	}

}

