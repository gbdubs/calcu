package calculus.utilities;

public class MenuItem {
	private String url;
	
	private String title;
	private String description;
	private String time;
	private String percentage;
	
	private String color;
	private String icon;
	private String image;
	
	public MenuItem(String url, String title, String description, String time, String percentage, String color, String icon, String image){
		this.url = url;
		this.title = title;
		this.description = description;
		this.time = time;
		this.percentage = percentage;
		this.color = color;
		this.icon = icon;
		this.image = image;
	}
	
	public String getUrl(){
		return this.url;
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
