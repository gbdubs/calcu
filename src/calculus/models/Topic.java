package calculus.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import calculus.api.TagAPI;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Topic {

	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static AsyncDatastoreService asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();
	
	private String uuid;
	private List<String> subTopics;
	private List<String> parentTopics;
	private String title;
	private String shortDescription;
	private String longDescription;
	private String tags;
	private int difficulty;
	
	private boolean changedTitle;
	private boolean changedTags;
	private String originalTags;
	
	public Topic(String uuid) throws EntityNotFoundException{
		this(datastore.get(KeyFactory.createKey("Topic", uuid)));
	}
	
	public Topic(Entity e){
		this.uuid = (String) e.getProperty("uuid");
		this.subTopics = (List<String>) e.getProperty("subTopics");
		if (subTopics == null){ subTopics = new ArrayList<String>(); }
		this.parentTopics = (List<String>) e.getProperty("parentTopics");
		if (parentTopics == null){ parentTopics = new ArrayList<String>(); }
		this.title = (String) e.getProperty("title");
		this.shortDescription = (String) e.getProperty("shortDescription");
		this.longDescription = ((Text) e.getProperty("longDescription")).getValue();
		this.tags = (String) e.getProperty("tags");
		this.originalTags = tags;
		this.difficulty = (int) ((Long) e.getProperty("difficulty")).intValue();
		this.changedTags = false;
		this.changedTitle = false;
	}
	
	public Topic(JsonObject jo){
		uuid = UUID.randomUUID().toString();
		JsonElement uuidElement = jo.get("uuid");
		if (uuidElement != null){
			uuid = uuidElement.getAsString();
		}
		
		this.title = jo.get("title").getAsString();
		this.shortDescription = jo.get("shortDescription").getAsString();
		this.longDescription = jo.get("longDescription").getAsString();
		this.tags = jo.get("tags").getAsString();
		this.difficulty = jo.get("difficulty").getAsInt();
		
		JsonArray ja = jo.get("parentTopics").getAsJsonArray();
		this.parentTopics = new ArrayList<String>();
		for (JsonElement je : ja){
			parentTopics.add(je.getAsString());
		}
		
		ja = jo.get("subTopics").getAsJsonArray();
		this.subTopics = new ArrayList<String>();
		for (JsonElement je : ja){
			subTopics.add(je.getAsString());
		}
		
		this.changedTags = true;
		this.changedTitle = true;
		this.originalTags = "";
	}
	
	public Topic(){
		//GSON EMPTY CONSTRUCTOR -- DO NOT USE.
	}
	
	public void save(){
		Entity e = preSave();
		datastore.put(e);
		postSave();
	}
	
	public void saveAsync() {
		Entity e = preSave();
		asyncDatastore.put(e);
		postSave();
	}

	private Entity preSave(){
		Entity e = new Entity(KeyFactory.createKey("Topic", uuid));
		e.setUnindexedProperty("uuid", uuid);
		e.setUnindexedProperty("subTopics", subTopics);
		e.setUnindexedProperty("parentTopics", parentTopics);
		e.setUnindexedProperty("title", title);
		e.setUnindexedProperty("shortDescription", shortDescription);
		e.setUnindexedProperty("longDescription", new Text(this.longDescription));
		e.setUnindexedProperty("tags", tags);
		e.setUnindexedProperty("difficulty", new Long(difficulty));
		return e;
	}

	private void postSave(){
		if (changedTitle){
			updateTitleMappingEntity(title);
		}
		
		if (changedTags){
			updateTagMappings();
		}
	}

	private void updateTagMappings() {
		Set<String> originals = new HashSet<String>();
		for (String s : originalTags.split(",")){
			originals.add(s.trim().toLowerCase());
		}
		Set<String> current = new HashSet<String>();
		for (String s : tags.split(",")){
			current.add(s.trim().toLowerCase());
		}
		Set<String> toRemove = new HashSet<String>(originals);
		toRemove.removeAll(current);
		
		Set<String> toAdd = new HashSet<String>(current);
		toAdd.removeAll(originals);
		
		for (String t : toRemove){
			TagAPI.removeTopicFrimTag(uuid, t);
		}
		
		for (String t : toAdd){
			TagAPI.addNewTopicToTag(uuid, t);
		}
	}

	public static Entity getTopicTitleMapping(){
		Entity mapping;
		try {
			mapping = datastore.get(KeyFactory.createKey("Topic", "TopicTitleMapping"));
		} catch (EntityNotFoundException e) {
			mapping = new Entity(KeyFactory.createKey("Topic", "TopicTitleMapping"));
		}
		return mapping;
	}
	
	private void updateTitleMappingEntity(String newTitle){
		Entity mapping = getTopicTitleMapping();
		if (mapping.hasProperty(uuid)){
			String oldName = (String) mapping.getProperty(uuid);
			mapping.removeProperty(oldName);
		}
		mapping.setUnindexedProperty(uuid, newTitle);
		mapping.setUnindexedProperty(newTitle, uuid);
		asyncDatastore.put(mapping);
	}
	
	public static Topic createNewTopic(String title, String shortDescription, String longDescription, String tags){
		Topic t = new Topic(title, shortDescription, longDescription, tags);
		return t;
	}
	
	private Topic(String title, String shortDescription, String longDescription, String tags){
		this.uuid = UUID.randomUUID().toString();
		this.title = title;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.originalTags = "";
		this.tags = tags;
		this.subTopics = new ArrayList<String>();
		this.parentTopics = new ArrayList<String>();
		changedTitle = true;
		changedTags = true;
	}
	
	
	public String getUuid(){
		return uuid;
	}
	
	public String getTitle(){
		return title;
	}
	
	public List<String> getSubTopics(){
		return subTopics;
	}
	
	public List<String> getParentTopics(){
		return parentTopics;
	}
	
	public String getShortDescription(){
		return shortDescription;
	}
	
	public String getLongDescription(){
		return longDescription;
	}
	
	public String getTags(){
		return tags;
	}
	
	public void setTitle(String title){
		this.title = title;
		changedTitle = true;
	}
	
	public void addSubTopic(String subTopic){
		if (this.subTopics.contains(subTopic)){
			this.subTopics.add(subTopic);
		}
	}
	
	public void addParentTopic(String parentTopic){
		if (this.parentTopics.contains(parentTopic)){
			this.parentTopics.add(parentTopic);
		}
	}
	
	public void setShortDescription(String s){
		this.shortDescription = s;
	}
	
	public void setLongDescription(String s){
		this.longDescription = s;
	}
	
	public void addTag(String tag){
		if (this.tags.length() != 0){
			this.tags = this.tags + ", " + tag;
		} else {
			this.tags = tag;
		}
		this.changedTags = true;
	}
	
	public void setTags(String s){
		this.tags = s;
		this.changedTags = true;
	}

	public int getDifficulty(){
		return difficulty;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
}
	
