package calculus.models;

import java.util.ArrayList;
import java.util.List;

import calculus.api.TextContentAPI;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class TextContent extends Content {

	private static List<String> FIELDS = new ArrayList<String>();
	
	static {
		FIELDS.addAll(Content.FIELDS);
	}	
	
	public TextContent(String uuid) {
		super(uuid, "textContent");
	}
	
	public TextContent(Key key) {
		super(key, "textContent");
	}
	
	public TextContent(Entity entity) {
		super(entity, "textContent");
	}
	
	public String getEditUrl(){
		return "/contribute/text-content/edit/" + this.getUuid();
	}
	
	@Override
	public void verifyAcceptableProperty(String property) {
		boolean acceptableProperty = false;
		for(String realProperties : FIELDS){
			if (realProperties.equals(property)) acceptableProperty = true;
		}
		if (!acceptableProperty) throw new RuntimeException("Unacceptable Property Modification.");
	}
	
	public List<Answer> getAnswers(){
		return TextContentAPI.getAnswersForTextContent(this);
	}

}
