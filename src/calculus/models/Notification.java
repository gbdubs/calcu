package calculus.models;

import java.text.SimpleDateFormat;
import java.util.UUID;

import calculus.api.UserPublicInfoAPI;
import calculus.utilities.MenuItem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Notification {

	private String uuid;
	private String recipientId;
	private String title;
	private String body;
	private String associatedUserId;
	private String url;
	private long time;
	private String color;
	
	public Notification(){
		this.uuid = UUID.randomUUID().toString();
	}
	
	public Notification(String uuid){
		this.uuid = uuid;
	}
	
	public String getRecipientId(){
		return recipientId;
	}
	
	public Notification withRecipientId(String id){
		this.recipientId = id;
		return this;
	}

	public String getTitle(){
		return title;
	}
	
	public Notification withTitle(String title){
		this.title = title;
		return this;
	}
	
	public String getBody(){
		return body;
	}
	
	public Notification withBody(String body){
		this.body = body;
		return this;
	}
	
	public String getAssociatedUserId(){
		return this.associatedUserId;
	}

	public Notification withAssociatedUserId(String id){
		this.associatedUserId = id;
		return this;
	}
	
	public String getUrl(){
		return url;
	}
	
	public Notification withUrl(String url){
		this.url = url;
		return this;
	}
	
	public long getTime(){
		return time;
	}
	
	public Notification withTime(long l){
		this.time = l;
		return this;
	}
	
	public Notification withTimeNow(){
		this.time = System.currentTimeMillis();
		return this;
	}
	
	public String getColor(){
		return color;
	}
	
	public Notification withColor(String color){
		this.color = color;
		return this;
	}
	
	public MenuItem getMenuItem(){
		String userProfPic = UserPublicInfoAPI.getProfilePictureUrl(this.associatedUserId);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM-dd-yy");
		String dateTime = sdf.format(time);
		return new MenuItem(url, uuid, title, body, dateTime, "", color, "", userProfPic);
	}
	
	public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this).toString();
	}
	
	public static Notification fromJson(String s){
		JsonParser parser = new JsonParser();
		JsonElement result = parser.parse(s);
		JsonObject object = result.getAsJsonObject();
		
		String uuid = object.get("uuid").getAsString();
		String recipientId = object.get("recipientId").getAsString();
		String title = object.get("title").getAsString();
		String body = object.get("body").getAsString();
		String associatedUserId = object.get("associatedUserId").getAsString();
		String url = object.get("url").getAsString();
		long time = object.get("time").getAsLong();
		String color = object.get("color").getAsString();
		
		Notification toReturn = new Notification(uuid)
			.withRecipientId(recipientId).withBody(body)
			.withTitle(title).withAssociatedUserId(associatedUserId)
			.withUrl(url).withTime(time).withColor(color);
		
		return toReturn;
	}
	
	public String getUuid(){
		return uuid;
	}
}
