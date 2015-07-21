package calculus.topic;

import java.util.ArrayList;
import java.util.List;

import calculus.utilities.UuidTools;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class TopicAPI {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public static Topic getOrCreateTopicFromUrl(String topicUrl){
		Entity titleMapping = Topic.getTopicTitleMapping();
		
		String title = topicUrl.substring(topicUrl.lastIndexOf('/') + 1);
		
		if (titleMapping.getProperty(title) != null){
			return getTopicByUUID((String) titleMapping.getProperty(title));
		} 
		Topic result = getTopicByUUID(createTopicFromUrl(topicUrl));
		
		titleMapping.setUnindexedProperty(result.getTitle(), result.getUuid());
		
		datastore.put(titleMapping);
		
		return result;
	}

	private static Topic getTopicByName(String name){
		Entity mapping = Topic.getTopicTitleMapping();
		return getTopicByUUID((String) mapping.getProperty(name));
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
			Topic t = Topic.createNewTopic(topicTitles[topicTitles.length - 1], "AUTO GENERATED", "AUTO GENERATED", "AUTO GENERATED");
			
			if (topicTitles.length > 1){
				String parental = topicTitles[0];
				for (int i = 1; i < topicTitles.length - 1; i++){
					parental += "/" + topicTitles[i];
				}
				Topic parent = getOrCreateTopicFromUrl(parental);
				parent.addSubTopic(t.getUuid());
				t.addParentTopic(parent.getUuid());
				parent.save();
			}
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

	public static List<Topic> getAllTopics() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Topic> topics = new ArrayList<Topic>();
		Query q = new Query("Topic");
		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()){
			Topic t = new Topic(e);
			if (t != null){
				topics.add(t);
			}
		}
		return topics;
	}
	
	public static List<Topic> getAllRootTopics(){
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
}

