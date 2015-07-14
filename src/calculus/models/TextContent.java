package calculus.models;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class TextContent extends Content {
	
	// GSON CONSTRUCTOR -- DO NOT USE
	public TextContent(){
		
	}
	
	public TextContent(String uuid) throws EntityNotFoundException {
		super(uuid, "textContent");
	}
	
	public TextContent(Entity entity) {
		super(entity, "textContent");
	}
	
	public String getEditUrl(){
		return "/contribute/text-content/edit/" + this.getUuid();
	}

	@Override
	public void setTypeSpecificEntityProperties() {
		// TextContent does not have any special properties!
	}
}
