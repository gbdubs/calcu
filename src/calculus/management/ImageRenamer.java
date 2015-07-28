package calculus.management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ImageRenamer {

	
	public static void main(String[] args){
		Map<String, String> mapping = createMapping(allImages(new File("war/_static/img/content")));
		System.out.println(mapping);
		//moveFiles(mapping);
		replaceMappingsInFiles(mapping);
	}
	
	public static List<String> allImages(File root){
		File[] subFiles = root.listFiles();
		List<String> images = new ArrayList<String>();
		for (File f : subFiles){
			if (f.exists() && !f.isHidden() && f.isFile() && f.getName().substring(f.getName().length() - 4).equalsIgnoreCase(".png")){
				images.add(f.getAbsolutePath());
			} else if (f.exists() && !f.isHidden() && f.isDirectory()){
				images.addAll(allImages(f));
			}
		}
		return images;
	}
	/*
	public static Map<String, String> createMapping(List<String> images){
		int count = 0;
		int folderNumber = 0;
		Map<String, String> mapping = new HashMap<String, String>();
		
		for (String image : images){
			count++;
			if (count >= 900){
				folderNumber++;
				count = 0;
			}
			mapping.put("/"+image, "/"+ folderNumber + "/" + image);	
		}
		System.out.println(mapping);
		
		return mapping;
	}
	*/
	
	public static Map<String, String> createMapping(List<String> images){
		Map<String, String> results = new HashMap<String, String>();
		for (String image : images){
			File f = new File (image);
			String toReplace = "/" + f.getParentFile().getName() + "/" + f.getName();
			results.put("/" + f.getName(), toReplace);
		}
		return results;
	}
	
	
	public static void moveFiles(Map<String, String> mapping){
	    String parentDir = "war/_static/img/content";
		for (String source : mapping.keySet()){
			File f = new File(parentDir + source);
			f.renameTo(new File(parentDir + mapping.get(source)));
		}
	}
	
	public static void replaceMappingsInFiles(Map<String, String> mappings){
		for (int i = 1; i <= 7; i++){
			replaceMappingsInFile(new File("war/WEB-INF/data/content/state-file-" + i + ".txt"), mappings);
		}
	}
	
	public static void replaceMappingsInFile(File file, Map<String, String> mappings){
		StringBuilder result = new StringBuilder();
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			return;
		}
		while (scan.hasNextLine()){
			String line = scan.nextLine();
			if (line.contains("img")){
				for (String key : mappings.keySet()){
					line = line.replace(key, mappings.get(key));
				}
			}
			result.append("\n" +line);
		}
		scan.close();
		File newFile = new File(file.getAbsolutePath());
		try {
			PrintWriter pw = new PrintWriter(newFile);
			pw.println(result.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			return;
		}
	}
}
