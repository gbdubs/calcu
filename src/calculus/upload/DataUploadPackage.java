package calculus.upload;

import java.util.List;

import calculus.models.Achievement;
import calculus.models.Content;
import calculus.models.Topic;

public class DataUploadPackage {

	private List<Achievement> achievements;
	private List<Content> content;
	private List<Topic> topics;
	
	public DataUploadPackage(){
		
	}
	
	public void asyncSave(){
		for (Content c : content){
			c.saveAsync();
			System.out.println("SAVED CONTENT WITH UUID: " + c.getUuid());
		}
		
		for (Topic t : topics){
			t.saveAsync();
			System.out.println("SAVED TOPIC WITH UUID: " + t.getUuid());
		}
		
		for (Achievement a : achievements){
			a.saveAsync();
			System.out.println("SAVED ACHEIVEMENT WITH UUID: " + a.getUuid());
		}
	}
	
}
