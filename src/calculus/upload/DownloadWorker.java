package calculus.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		DataUploadPackage dup = DataUploadPackage.getSiteContent();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		writer.println(gson.toJson(dup));
	}
}
