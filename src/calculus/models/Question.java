package calculus.models;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class Question extends Content{
	
	// GSON CONSTRUCTOR -- DO NOT USE
	public Question(){
		
	}
	
	public Question(String uuid) throws EntityNotFoundException {
		super(uuid, "question");
	}
	
	public Question(Entity entity) {
		super(entity, "question");
	}
	

	public String getEditUrl(){
		return "/contribute/question/edit/" + this.getUuid();
	}

	@Override
	public void setTypeSpecificEntityProperties() {
		// Questions have no special properties!
	}

	@Override
	public void patchLatexTypeSpecificProperties() {
		// Questions have no special properties!
	}
}
