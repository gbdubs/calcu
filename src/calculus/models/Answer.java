package calculus.models;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class Answer extends Content {
	
	private boolean approved;
	private String parentUuid;
	
	// GSON CONSTRUCTOR -- DO NOT USE
	public Answer(){
			
	}
	
	public Answer(String uuid) throws EntityNotFoundException {
		super(uuid, "answer");
		postContentConstructor();
	}
	
	public Answer(Entity entity) {
		super(entity, "answer");
		postContentConstructor();
	}

	private void postContentConstructor(){
		this.approved = (boolean) this.entity.getProperty("approved");
		this.parentUuid = (String) this.entity.getProperty("parentUuid");
	}
	
	public boolean getApproved(){
		return (boolean) this.entity.getProperty("approved");
	}
	
	public String getIcon(){
		if (this.getApproved()){
			return "fa-check";
		} else {
			return "fa-lightbulb-o";
		}
	}
	
	public String getColorClass(){
		if (this.getApproved()){
			return "box-success";
		} else {
			return "box-warning";
		}
	}
	
	public String getParentUuid(){
		return parentUuid;
	}
	
	public String getApprovedSolutionUrl(){
		return "/mark-approved-answer/" + this.getUuid();
	}
	
	public String getNotApprovedSolutionUrl(){
		return "/mark-approved-answer/not/" + this.getUuid();
	}

	public void markApproved() {
		this.approved = true;
		this.save();
	}
	
	public void markNotApproved() {
		this.approved = false;
		this.save();
	}

	@Override
	public void setTypeSpecificEntityProperties() {
		this.entity.setUnindexedProperty("approved", this.approved);
		this.entity.setUnindexedProperty("parentUuid", this.parentUuid);
	}

	@Override
	public void patchLatexTypeSpecificProperties() {
		// No latex special fields
	}
	
}

