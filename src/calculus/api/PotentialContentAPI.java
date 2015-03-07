package calculus.api;

import calculus.models.PotentialContent;

public class PotentialContentAPI {
	
	public String createContentFromPotential(String uuid, String contentType){
		PotentialContent pc = PotentialContent.get(uuid);
		if (pc == null) return null;
		if (contentType.equals("practiceProblem")){
			return createPracticeProblemFromPotential(pc);
		} else if (contentType.equals("question")){
			return createQuestionFromPotential(pc);
		} else if (contentType.equals("textContent")){
			return createTextContentFromPotential(pc);
		} else {
			return null;
		}
	}

	private String createPracticeProblemFromPotential(PotentialContent pc){
		String title = pc.getTitle();
		String body = pc.getBody();
		String solution = pc.getSolution();
		String tags = pc.getTagsAsString();
		String source = pc.getSource();
		String newUuid = PracticeProblemAPI.createNewPracticeProblemFromUpload(title, body, solution, tags, source);
		return newUuid;
	}
	
	
	private String createQuestionFromPotential(PotentialContent pc){
		String title = pc.getTitle();
		String body = pc.getBody();
		String tags = pc.getTagsAsString();
		String source = pc.getSource();
		String newUuid = QuestionAPI.createNewQuestionFromUpload(title, body, tags, source);
		return newUuid;
	}
	
	private String createTextContentFromPotential(PotentialContent pc){
		String title = pc.getTitle();
		String body = pc.getBody();
		String tags = pc.getTagsAsString();
		String source = pc.getSource();
		String newUuid = TextContentAPI.createNewTextContentFromUpload(title, body, tags, source);
		return newUuid;
	}
	
}
