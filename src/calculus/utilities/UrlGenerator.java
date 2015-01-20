package calculus.utilities;

import com.google.appengine.api.users.User;

public class UrlGenerator {
	
	public static String profileUrl(String userId){
		return "/user/" + userId; 
	}
	
	public static String profileUrl(User user){
		return "/user/" + user.getUserId(); 
	}
}
