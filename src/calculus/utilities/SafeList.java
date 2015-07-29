package calculus.utilities;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class SafeList {

	@SuppressWarnings("unchecked")
	public static List<String> string(Entity e, String property){
		try{
			List<String> result = (List<String>) e.getProperty(property);
			if (result == null){
				return new ArrayList<String>();
			}
			return result;
		} catch (Exception exception){
			return new ArrayList<String>();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Text> text(Entity e, String property) {
		try{
			List<Text> result = (List<Text>) e.getProperty(property);
			if (result == null){
				return new ArrayList<Text>();
			}
			return result;
		} catch (Exception exception){
			return new ArrayList<Text>();
		}
	}
	
}
