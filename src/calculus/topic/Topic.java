package calculus.topic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import calculus.api.TagAPI;
import calculus.utilities.SafeList;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

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
	
	private List<String> contentUuids;
	
	public Topic(String uuid) throws EntityNotFoundException{
		this(datastore.get(KeyFactory.createKey("Topic", uuid)));
	}
	
	public Topic(Entity e){
		this.uuid = (String) e.getProperty("uuid");
		this.subTopics = SafeList.string(e, "subTopics");
		if (subTopics == null){ subTopics = new ArrayList<String>(); }
		this.parentTopics = SafeList.string(e, "parentTopics");
		this.contentUuids = SafeList.string(e, "contentUuids");
		if (parentTopics == null){ parentTopics = new ArrayList<String>(); }
		this.title = (String) e.getProperty("title");
		this.shortDescription = (String) e.getProperty("shortDescription");
		this.longDescription = ((Text) e.getProperty("longDescription")).getValue();
		this.tags = (String) e.getProperty("tags");
		this.difficulty = (int) ((Long) e.getProperty("difficulty")).intValue();
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
		e.setUnindexedProperty("contentUuids", contentUuids);
		return e;
	}

	private void postSave(){
		
	}

	public List<Topic> getFamilyTopics(){
		List<String> family = new ArrayList<String>();
		family.addAll(parentTopics);
		family.addAll(subTopics);
		return TopicAPI.getTopicsAsync(family);
	}


	public static Entity getTopicTitleMapping(){
		Entity mapping;
		try {
			mapping = datastore.get(KeyFactory.createKey("TopicTitleMapping", "TopicTitleMapping"));
		} catch (EntityNotFoundException e) {
			mapping = new Entity(KeyFactory.createKey("TopicTitleMapping", "TopicTitleMapping"));
		}
		return mapping;
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
		this.tags = tags;
		this.subTopics = new ArrayList<String>();
		this.parentTopics = new ArrayList<String>();
	}
	
	public Topic(EmbeddedEntity ee) {
		Entity e = new Entity(ee.getKey());
		e.setPropertiesFrom(ee);
		this.uuid = (String) e.getProperty("uuid");
		this.subTopics = SafeList.string(e, "subTopics");
		if (subTopics == null){ subTopics = new ArrayList<String>(); }
		this.parentTopics = SafeList.string(e, "parentTopics");
		this.contentUuids = SafeList.string(e, "contentUuids");
		if (parentTopics == null){ parentTopics = new ArrayList<String>(); }
		this.title = (String) e.getProperty("title");
		this.shortDescription = (String) e.getProperty("shortDescription");
		this.longDescription = ((Text) e.getProperty("longDescription")).getValue();
		this.tags = (String) e.getProperty("tags");
		this.difficulty = (int) ((Long) e.getProperty("difficulty")).intValue();
	}

	public String getUuid(){
		return uuid;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getUpperCaseTitle(){
		String s = "";
		if (title.length() == 0){
			return s;
		}
		for (String word : title.toLowerCase().split(" ")){
			s += (" " + word.charAt(0)).toUpperCase() + word.substring(1);
		}
		return s.trim();
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
	
	public String getUpperCaseTags(){
		String s = "";
		if (tags.length() < 3){
			return s;
		}
		for (String word : tags.toLowerCase().split(" ")){
			if (word.length() > 0){
				s += (" " + word.charAt(0)).toUpperCase() + word.substring(1);
			} else {
				s += " " + word;
			}
		}
		return s.trim();
	}
	
	public List<String> getUpperCaseTagsAsList(){
		List<String> result = new ArrayList<String>();
		for (String tag : getUpperCaseTags().split(",")){
			if (!tag.equalsIgnoreCase("auto generated")){
				result.add(tag.trim());
			}
		}
		return result;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
	
	public List<String> getContentUuids(){
		return contentUuids;
	}

	public void addContentUuid(String uuid){
		if (contentUuids == null){
			contentUuids = new ArrayList<String>();
		}
		contentUuids.add(uuid);
	}
	
	void removeContentUuid(String uuid){
		if (contentUuids == null){
			return;
		}
		contentUuids.remove(uuid);
	}
	
	void setTitle(String newTitle){
		this.title = newTitle;
		Entity mapping = getTopicTitleMapping();
		if (mapping.hasProperty(uuid)){
			String oldName = (String) mapping.getProperty(uuid);
			mapping.removeProperty(oldName);
		}
		mapping.setUnindexedProperty(uuid, newTitle);
		mapping.setUnindexedProperty(newTitle, uuid);
		asyncDatastore.put(mapping);
	}
	
	void addSubTopic(String subTopic){
		if (!this.subTopics.contains(subTopic)){
			this.subTopics.add(subTopic);
		}
	}
	
	void addParentTopic(String parentTopic){
		if (!this.parentTopics.contains(parentTopic)){
			this.parentTopics.add(parentTopic);
		}
	}
	
	void setShortDescription(String s){
		this.shortDescription = s;
	}
	
	void setLongDescription(String s){
		this.longDescription = s;
	}
	
	void setTags(String newTags){
		Set<String> originals = new HashSet<String>();
		for (String s : tags.split(",")){
			originals.add(s.trim().toLowerCase());
		}
		Set<String> current = new HashSet<String>();
		for (String s : newTags.split(",")){
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
		
		this.tags = newTags;
	}

	void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public EmbeddedEntity getEmbeddedEntity() {
		EmbeddedEntity ee = new EmbeddedEntity();
		ee.setPropertiesFrom(preSave());
		return ee;
	}
}
	
