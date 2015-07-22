package calculus.upload;

import java.util.List;

import com.google.gson.Gson;

import calculus.api.AchievementsAPI;
import calculus.api.AnswersAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.TextContentAPI;
import calculus.api.TopicAPI;
import calculus.models.Achievement;
import calculus.models.Answer;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.models.Topic;

public class DataUploadPackage {

	private List<Achievement> achievements;
	private List<PracticeProblem> practiceProblems;
	private List<Question> questions;
	private List<TextContent> textContent;
	private List<Answer> answers;
	private List<Topic> topics;
	
	public DataUploadPackage(){
		
	}
	
	public void asyncSave(){
		long l = System.currentTimeMillis();
		
		for (Content pp : practiceProblems){
			if (pp != null){
				pp.saveAsync();
				System.out.println("SAVED PRACTICE PROBLEM WITH UUID: " + pp.getUuid());
			}
		}
		
		for (Content q : questions){
			if (q != null){
				q.saveAsync();
				System.out.println("SAVED QUESTION WITH UUID: " + q.getUuid());
			}
		}
		
		for (Content tc : textContent){
			if (tc != null){
				tc.saveAsync();
				System.out.println("SAVED TEXT CONTENT WITH UUID: " + tc.getUuid());
			}
		}
		
		for (Content a : answers){
			if (a != null){
				a.saveAsync();
				System.out.println("SAVED ANSWER WITH UUID: " + a.getUuid());
			}
		}
		
		for (Topic t : topics){
			if (t != null){
				t.saveAsync();
				System.out.println("SAVED TOPIC WITH UUID: " + t.getUuid());
			}
		}
		
		for (Achievement a : achievements){
			if (a != null){
				a.saveAsync();
				System.out.println("SAVED ACHEIVEMENT WITH UUID: " + a.getUuid());
			}
		}
		
		System.out.println("UPLOAD TOOK: " + (System.currentTimeMillis() - l) + " milliseconds.");
	}
	
	public static DataUploadPackage getSiteContent(){
		DataUploadPackage dup = new DataUploadPackage();
		dup.questions = QuestionAPI.getAllQuestions();
		dup.practiceProblems = PracticeProblemAPI.getAllPracticeProblems();
		dup.textContent = TextContentAPI.getAllTextContent();
		dup.answers = AnswersAPI.getAllAnswers();
		dup.topics = TopicAPI.getAllTopics();
		dup.achievements = AchievementsAPI.getAllAchievements();
		return dup;
	}
	
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public void patchLatex() {
		for (PracticeProblem pp : practiceProblems){
			pp.patchLatex();
		}
		for (Question q : questions){
			q.patchLatex();
		}
		for (TextContent tc : textContent){
			tc.patchLatex();
		}
		for (Answer a : answers){
			a.patchLatex();
		}
	}
	
}
