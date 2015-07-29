package calculus.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import calculus.api.AchievementsAPI;
import calculus.api.AnswersAPI;
import calculus.api.PracticeProblemAPI;
import calculus.api.QuestionAPI;
import calculus.api.TextContentAPI;
import calculus.models.Achievement;
import calculus.models.Answer;
import calculus.models.Content;
import calculus.models.PracticeProblem;
import calculus.models.Question;
import calculus.models.TextContent;
import calculus.topic.Topic;
import calculus.topic.TopicAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataUploadPackage {

	private List<Achievement> achievements;
	private List<PracticeProblem> practiceProblems;
	private List<Question> questions;
	private List<TextContent> textContent;
	private List<Answer> answers;
	private List<Topic> topics;
	
	public DataUploadPackage(){
		achievements = new ArrayList<Achievement>();
		practiceProblems = new ArrayList<PracticeProblem>();
		questions = new ArrayList<Question>();
		textContent = new ArrayList<TextContent>();
		answers = new ArrayList<Answer>();
		topics = new ArrayList<Topic>();
	}
	
	public void asyncSave(){
		long l = System.currentTimeMillis();
		
		int count = 0;
		for (Content pp : practiceProblems){
			if (pp != null){
				pp.saveAsync();
				count++;
			}
		}
		System.out.println("SAVED PRACTICE PROBLEMS : " + count);
		
		count = 0;
		for (Content q : questions){
			if (q != null){
				q.saveAsync();
				count++;
			}
		}
		System.out.println("SAVED QUESTIONS : " + count);
		
		count = 0;
		for (Content tc : textContent){
			if (tc != null){
				tc.saveAsync();
				count++;
			}
		}
		System.out.println("SAVED TEXT CONTENT : " + count);
		
		count = 0;
		for (Content a : answers){
			if (a != null){
				a.saveAsync();
				count++;
			}
		}
		System.out.println("SAVED ANSWERS : " + count);
		
		count = 0;
		for (Topic t : topics){
			if (t != null){
				t.saveAsync();
				count++;
			}
		}
		System.out.println("SAVED TOPICS : " + count);
		
		count = 0;
		for (Achievement a : achievements){
			if (a != null){
				a.saveAsync();
				count++;
			}
		}
		System.out.println("SAVED ACHIEVEMENTS : " + count);
		
		System.out.println("UPLOAD TOOK: " + (System.currentTimeMillis() - l) + " milliseconds.");
	}
	
	public static DataUploadPackage getSiteContent(){
		DataUploadPackage dup = new DataUploadPackage();
		dup.questions = QuestionAPI.getAllQuestions();
		dup.practiceProblems = PracticeProblemAPI.getAllPracticeProblems();
		dup.textContent = TextContentAPI.getAllTextContent();
		dup.answers = AnswersAPI.getAllAnswers();
		dup.topics = TopicAPI.downloadAllTopics();
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
	
	public void cleanForHtml() {
		for (PracticeProblem pp : practiceProblems){
			pp.cleanForHtml();
		}
		for (Question q : questions){
			q.cleanForHtml();
		}
		for (TextContent tc : textContent){
			tc.cleanForHtml();
		}
		for (Answer a : answers){
			a.cleanForHtml();
		}
	}
	
	public int size(){
		return achievements.size() + practiceProblems.size() + questions.size() + textContent.size() + answers.size() + topics.size();
	}
	
	
	
	
	public void addAll(DataUploadPackage other) {
		this.achievements.addAll(other.achievements);
		this.practiceProblems.addAll(other.practiceProblems);
		this.questions.addAll(other.questions);
		this.textContent.addAll(other.textContent);
		this.answers.addAll(other.answers);
		this.topics.addAll(other.topics);
	}

	public DataUploadPackage split(){
		DataUploadPackage ndup = new DataUploadPackage();
		while(ndup.achievements.size() < achievements.size()){
			ndup.achievements.add(achievements.remove(0));
		}
		while(ndup.practiceProblems.size() < practiceProblems.size()){
			ndup.practiceProblems.add(practiceProblems.remove(0));
		}
		while(ndup.questions.size() < questions.size()){
			ndup.questions.add(questions.remove(0));
		}
		while(ndup.textContent.size() < textContent.size()){
			ndup.textContent.add(textContent.remove(0));
		}
		while(ndup.topics.size() < topics.size()){
			ndup.topics.add(topics.remove(0));
		}
		while(ndup.answers.size() < answers.size()){
			ndup.answers.add(answers.remove(0));
		}
		return ndup;
	}

	public static void main(String[] args){
		mergeAllMainFiles();
		splitAllMainFiles();
	}

	public static void mergeAllMainFiles(){
		DataUploadPackage stateFileOne = getFileContents("war/WEB-INF/data/content/state-file-1.txt");
		DataUploadPackage stateFileTwo = getFileContents("war/WEB-INF/data/content/state-file-2.txt");
		DataUploadPackage stateFileThree = getFileContents("war/WEB-INF/data/content/state-file-3.txt");
		DataUploadPackage stateFileFour = getFileContents("war/WEB-INF/data/content/state-file-4.txt");
		DataUploadPackage stateFileFive = getFileContents("war/WEB-INF/data/content/state-file-5.txt");
		DataUploadPackage stateFileSix = getFileContents("war/WEB-INF/data/content/state-file-6.txt");
		DataUploadPackage stateFileSeven = getFileContents("war/WEB-INF/data/content/state-file-7.txt");
		
		stateFileOne.addAll(stateFileTwo);
		stateFileOne.addAll(stateFileThree);
		stateFileOne.addAll(stateFileFour);
		stateFileOne.addAll(stateFileFive);
		stateFileOne.addAll(stateFileSix);
		stateFileOne.addAll(stateFileSeven);
		
		writeStringToFile(stateFileOne, "war/WEB-INF/data/content/all.txt");
	}
	
	public static void mergeAllEditedMainFiles(){
		DataUploadPackage stateFileOne = getFileContents("war/WEB-INF/data/content/state-file-1-edited-edited-edited-edited.txt");
		DataUploadPackage stateFileTwo = getFileContents("war/WEB-INF/data/content/state-file-2-edited-edited-edited-edited.txt");
		DataUploadPackage stateFileThree = getFileContents("war/WEB-INF/data/content/state-file-3-edited-edited-edited-edited.txt");
		DataUploadPackage stateFileFour = getFileContents("war/WEB-INF/data/content/state-file-4-edited-edited-edited-edited.txt");
		DataUploadPackage stateFileFive = getFileContents("war/WEB-INF/data/content/state-file-5-edited-edited-edited-edited.txt");
		DataUploadPackage stateFileSix = getFileContents("war/WEB-INF/data/content/state-file-6-edited-edited-edited-edited.txt");
		DataUploadPackage stateFileSeven = getFileContents("war/WEB-INF/data/content/state-file-7-edited-edited-edited-edited.txt");
		
		stateFileOne.addAll(stateFileTwo);
		stateFileOne.addAll(stateFileThree);
		stateFileOne.addAll(stateFileFour);
		stateFileOne.addAll(stateFileFive);
		stateFileOne.addAll(stateFileSix);
		stateFileOne.addAll(stateFileSeven);
		
		writeStringToFile(stateFileOne, "war/WEB-INF/data/content/all-edited-edited-edited-edited.txt");
	}

	public static void splitAllMainFiles(){
		DataUploadPackage stateFile = getFileContents("war/WEB-INF/data/content/all.txt");
		int i = 1;
		List<DataUploadPackage> distributed = distributeToSmallSizePackages(stateFile);
		for (DataUploadPackage dup : distributed){
			writeStringToFile(dup, "war/WEB-INF/data/content/digestable/" + i++ + ".txt");
		}
	}
	
	public static List<DataUploadPackage> distributeToSmallSizePackages(DataUploadPackage dup){
		List<DataUploadPackage> result = new ArrayList<DataUploadPackage>();
		result.add(dup);
		for (int i = 0; i < result.size(); i++){
			while (result.get(i).size() > 100){
				result.add(result.get(i).split());
			}
		}
		return result;
	}
	
	private static DataUploadPackage getFileContents(String fileName){
		String file = readFileToString(fileName);
		Gson gson = new Gson();
		DataUploadPackage dup = gson.fromJson(file, DataUploadPackage.class);
		return dup;
	}
	
	private static String readFileToString(String fileName){
		File f = new File(fileName);
		Scanner scan;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("THE FILE " + fileName + " COULD NOT BE FOUND.");
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			return "";
		}
		StringBuffer sb = new StringBuffer();
		while (scan.hasNextLine()){
			sb.append("\n");
			sb.append(scan.nextLine());
		}
		scan.close();
		return sb.toString();
	}
	
	private static void writeStringToFile(DataUploadPackage dup, String fileName){
		File f = new File(fileName);
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			System.err.println("THE FILE WAS NOT FOUND FOR PRINTING: " + fileName);
			return;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		pw.println(gson.toJson(dup));
		pw.close();
	}
	
}
