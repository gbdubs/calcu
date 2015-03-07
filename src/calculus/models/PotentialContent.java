package calculus.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class PotentialContent {

	private String uuid;
	private String title;
	private String body;
	private String solution;
	private List<String> tagList;
	private String tagString;
	private String source;
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public PotentialContent(){
		uuid = UUID.randomUUID().toString();
		title = "";
		body = "";
		solution = "";
		tagList = new ArrayList<String>();
		tagString = "";
	}

	public static PotentialContent get(String uuid){
		Key pcKey = KeyFactory.createKey("PotentialContent", uuid);
		try {
			Entity e = datastore.get(pcKey);
			return fromEntity(e);
		} catch (EntityNotFoundException enfe) {
			return null;
		}
	}

	public void save(){
		Key pcKey = KeyFactory.createKey("PotentialContent", this.uuid);
		Entity e;
		try {
			e = datastore.get(pcKey);
		} catch (EntityNotFoundException enfe) {
			e = new Entity(pcKey);
		}
		e.setUnindexedProperty("title", title);
		e.setUnindexedProperty("body", new Text(body));
		e.setUnindexedProperty("solution", new Text(solution));
		e.setUnindexedProperty("tags", tagString);
		e.setUnindexedProperty("source", source);
		
		datastore.put(e);
	}

	public static PotentialContent fromEntity(Entity e){
		String title = (String) e.getProperty("title");
		Text body = (Text) e.getProperty("body");
		Text solution = (Text) e.getProperty("solution");
		String tags = (String) e.getProperty("tags");
		String source = (String) e.getProperty("soruce");
		
		PotentialContent pc = new PotentialContent()
			.withTitle(title)
			.withBody(body.getValue())
			.withSolution(solution.getValue())
			.withTags(tags)
			.withSource(source);
		return pc;
	}

	public PotentialContent withTitle(String title){
		this.title = title;
		return this;
	}
	
	public PotentialContent withBody(String body){
		this.body = body;
		return this;
	}

	public PotentialContent withSolution(String solution){
		this.solution = solution;
		return this;
	}

	public PotentialContent withTags(List<String> tagList){
		this.tagList = tagList;
		String tagString = "";
		for (String tag : tagList){
			tagString = tagString + "," + tag;
		}
		this.tagString = tagString;
		return this;
	}
	
	public PotentialContent withTags(String tagString){
		this.tagString = tagString;
		List<String> tagList = Arrays.asList(tagString.split(","));
		this.tagList = tagList;
		return this;
	}

	public PotentialContent withSource(String source){
		this.source = source;
		return this;
	}
	
	public String getUuid(){
		return uuid;
	}

	public String getTitle(){
		return title;
	}

	public String getBody(){
		return body;
	}

	public String getSolution(){
		return solution;
	}
	
	public List<String> getTagsAsList(){
		return tagList;
	}
	
	public String getTagsAsString(){
		return tagString;
	}

	public String getSource(){
		return source;
	}
}
