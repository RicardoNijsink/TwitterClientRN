package nl.saxion.rn.projecttwitterclient;

import java.net.URL;

public class Tweet {
	private String createdAt, text, userName, location;
	private URL profileImage;
	
	
	public String getCreatedAt() {
		return createdAt;
	}
	public String getText() {
		return text;
	}
	public String getUserName() {
		return userName;
	}
	public String getLocation() {
		return location;
	}
	public URL getProfileImage() {
		return profileImage;
	}
	

	
}
