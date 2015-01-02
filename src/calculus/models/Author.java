package calculus.models;

import com.google.appengine.api.datastore.Entity;

import calculus.api.UserDatastoreAPI;
import calculus.utilities.KarmaDescription;

public class Author {

	private String username;
	private long karma;
	private String profilePictureUrl;
	private String profileUrl;
	private String userId;
	
	
	public Author(String authorId){
		Entity entity = UserDatastoreAPI.getUserPublicInfo(authorId);
		
		this.username = (String) entity.getProperty("username");
		this.karma = (long) entity.getProperty("karma");
		this.profilePictureUrl = (String) entity.getProperty("profilePictureUrl");
		this.profileUrl = (String) entity.getProperty("profileUrl");
		this.userId = (String) entity.getProperty("userId");
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getKarma(){
		return "" + this.karma;
	}
	
	public String getReadableKarma(){
		return KarmaDescription.toMediumString(((Long) this.karma).intValue());
	}
	
	public String getProfilePictureUrl(){
		return this.profilePictureUrl;
	}
	
	public String getProfileUrl(){
		return this.profileUrl;
	}
	
	public String getUserId(){
		return this.userId;
	}
}
