package calculus.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import calculus.api.ContentAPI;
import calculus.models.Content;
import calculus.models.Notification;

public class MenuItem {
	private String url;
	private String uuid;
	
	private String title;
	private String description;
	private String time;
	private String percentage;
	
	private String color;
	private String icon;
	private String image;
	
	public MenuItem(String url, String uuid, String title, String description, String time, String percentage, String color, String icon, String image){
		this.url = url;
		this.uuid = uuid;
		this.title = title;
		this.description = description;
		this.time = time;
		this.percentage = percentage;
		this.color = color;
		this.icon = icon;
		this.image = image;
	}
	
	public MenuItem(Content c) {
		this.url = c.getUrl();
		this.uuid = c.getUuid();
		this.title = c.getTitle();
		this.description = c.getBody();
		this.time = c.getShortReadableTime();
		this.percentage = "0%";
		
		String contentType = c.getContentType();
		
		this.icon = ContentAPI.getBoxIcon(contentType);
		this.color = ContentAPI.getBoxColor(contentType);
		
		this.image = "avatar2.png";
	}

	public String getUrl(){
		return this.url;
	}
	
	public String getUuid(){
		return this.uuid;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public String getIcon(){
		return this.icon;
	}
	
	public String getImage(){
		return this.image;
	}
	
	public String getTime(){
		return this.time;
	}
	
	public String getPercentage(){
		return this.percentage;
	}

	public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this).toString();
	}
	
	public static MenuItem fromJson(String s){
		JsonParser parser = new JsonParser();
		JsonElement result = parser.parse(s);
		JsonObject object = result.getAsJsonObject();
		
		String url = object.get("url").getAsString();
		String uuid = object.get("uuid").getAsString();
		String title = object.get("title").getAsString();
		String description = object.get("description").getAsString();
		String time = object.get("time").getAsString();
		String percentage = object.get("percentage").getAsString();
		String icon = object.get("icon").getAsString();
		String color = object.get("color").getAsString();
		String image = object.get("image").getAsString();
		
		MenuItem toReturn = new MenuItem(url, uuid, title, description, time, percentage, color, icon, image);
		
		return toReturn;
	}


}
