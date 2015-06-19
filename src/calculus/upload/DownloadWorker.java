package calculus.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.ContentAPI;
import calculus.models.Achievement;
import calculus.models.Content;
import calculus.models.Topic;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class DownloadWorker extends HttpServlet {

	private static final String BUCKETNAME = "calcu-us.appspot.com";
	private static final String FILENAME = "/downloads/current-state.txt";
	private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		writeToStateFile(resp);
	}
	
	private static void writeToStateFile(HttpServletResponse resp) throws IOException{
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production){
			GcsService gcsService = GcsServiceFactory.createGcsService();
		    GcsFilename filename = new GcsFilename(BUCKETNAME, FILENAME);
		    GcsFileOptions options = new GcsFileOptions.Builder()
		        .mimeType("text/plain")
		        .acl("public-read")
		        .build();
			
		    GcsOutputChannel writeChannel = gcsService.createOrReplace(filename, options);
		
		    PrintWriter writer = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		    printAll(writer);
		} else {
			// Return; we can't do anything. 
		}
	}
	
	public static void printAll(PrintWriter writer){
		writer.println("{");
		printAllContent(writer);
		printAllAchievements(writer);
		printAllTopics(writer);
		writer.println("}");
		writer.flush();
	}
	
	private static void printAllContent(PrintWriter pw){
		pw.println("\"Content\": [");
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		Query q = new Query("Content");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			Content c = ContentAPI.instantiateContent(e);
			
			pw.println(gson.toJson(c) + ",");
			
		}
		pw.println("],");
	}
	
	private static void printAllAchievements(PrintWriter pw){
		pw.println("\"Achievements\": [");
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		Query q = new Query("Achievement");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			Achievement a = new Achievement(e);
			pw.println(gson.toJson(a) + ",");
		}
		pw.println("],");
		
	}
	
	private static void printAllTopics(PrintWriter pw){
		pw.println("\"Topics\": [");
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		Query q = new Query("Topic");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity e : pq.asIterable()){
			Topic t = new Topic(e);
			pw.println(gson.toJson(t) + ",");
		}
		pw.println("]");
	}
}
