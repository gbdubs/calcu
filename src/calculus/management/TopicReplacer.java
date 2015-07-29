package calculus.management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TopicReplacer {

	public static void main(String[] args){
		Map<String, String> replacements = getReplacements(new File("/home/u/fall12/gward/Desktop/allTopics4.txt"), new File("/home/u/fall12/gward/Desktop/allTopics4-edited.txt"));
		List<String> keys = getListOfKeysInDescendingLengthOrder(replacements);
		System.out.println(replacements);
		System.out.println(keys);
		for (int i = 1; i <= 7; i++){
			File f = new File("/home/u/fall12/gward/Desktop/Google App Engine/calcu/war/WEB-INF/data/content/state-file-" +i+ "-edited-edited-edited.txt");
			makeReplacementsInFile(f, keys, replacements);
		}
	}
	
	public static class StringLengthComparator implements Comparator<String>{
		@Override
		public int compare(String o1, String o2) {
			return -1 * Integer.compare(o1.length(), o2.length());
		}
	}
	
	public static List<String> getListOfKeysInDescendingLengthOrder(Map<String, String> replacements){
		List<String> result = new ArrayList<String>();
		result.addAll(replacements.keySet());
		Collections.sort(result, new StringLengthComparator());
		return result;
	}
	
	public static Map<String, String> getReplacements(File original, File updated){
		Map<String, String> mapping = new HashMap<String, String>();

		try {
			Scanner originalScan = new Scanner(original);
			Scanner updatedScan = new Scanner(updated);
			
			while (originalScan.hasNextLine()){
				String oLine = originalScan.nextLine();
				String uLine = updatedScan.nextLine();
				mapping.put(oLine, uLine);
			}
			
			originalScan.close();
			updatedScan.close();
			
			return mapping;
		} catch (FileNotFoundException e) {
			System.err.println("FILES NOT FOUND TO GENERATE REPLACEMENTS");
			return null;
		}
	}
	
	public static void makeReplacementsInFile(File f, List<String> lookingFor, Map<String, String> replacements){
		try {
			Scanner scan = new Scanner(f);
			StringBuffer sb = new StringBuffer();
			while (scan.hasNextLine()){
				String line = scan.nextLine();
				if (line.contains("\"topic\":")){
					for (String looking : lookingFor){
						line = line.replace(looking, replacements.get(looking));
					}
				}
				sb.append("\n" + line);
			}
			scan.close();
			
			File toWrite = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".")) + "-edited.txt");
			PrintWriter pw = new PrintWriter(toWrite);
			pw.println(sb.toString());
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("COULDN'T FIND FILE FOR REPLACEMENTS");
			return;
		}
	}
	
}
