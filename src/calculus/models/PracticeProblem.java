package calculus.models;

import calculus.utilities.LatexPatcher;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Text;

public class PracticeProblem extends Content{

	private String authorSolution;
	
	// GSON CONSTRUCTOR -- DO NOT USE
	public PracticeProblem(){
		
	}
	
	public PracticeProblem (String uuid) throws EntityNotFoundException {
		super(uuid, "practiceProblem");
		postContentConstructor();
	}
	
	public PracticeProblem (Entity entity) {
		super(entity, "practiceProblem");
		postContentConstructor();
	}
	
	private void postContentConstructor(){
		this.authorSolution = ((Text) this.entity.getProperty("authorSolution")).getValue();
	}
	
	public String getAuthorSolution(){
		return authorSolution;
	}
	
	public String getEditUrl(){
		return "/contribute/practice-problem/edit/" + this.getUuid();
	}

	@Override
	public void setTypeSpecificEntityProperties() {
		this.entity.setProperty("authorSolution", new Text(authorSolution));
	}

	@Override
	public void patchLatexTypeSpecificProperties() {
		authorSolution = LatexPatcher.makeReplacements(authorSolution);
	}

	
}
