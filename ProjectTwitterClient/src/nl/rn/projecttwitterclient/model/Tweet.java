package nl.rn.projecttwitterclient.model;

import java.net.URL;
import java.util.ArrayList;

public class Tweet {
	private String createdAt, text, userName, location;
	private URL profileImage;
	private ArrayList<Entity> hashTags = new ArrayList<>();
	
	public Tweet(String createdAt, String text, String userName, String location) {
		this.createdAt = createdAt;
		this.text = text;
		this.userName = userName;
		this.location = location;
	}
	
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
	
	public ArrayList<Entity> getHashTags() {
		return hashTags;
	}
	
	public void addHashTag(HashTag hashTag) {
		hashTags.add(hashTag);
	}
	

	
}
