package calculus.utilities;

import calculus.api.ContentAPI;
import calculus.models.Content;

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
}
