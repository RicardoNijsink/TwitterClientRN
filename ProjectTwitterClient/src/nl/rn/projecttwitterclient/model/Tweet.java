package nl.rn.projecttwitterclient.model;

import java.net.URL;
import java.util.ArrayList;

public class Tweet {
	private String createdAt, text, location;
	private User user;
	private URL profileImage;
	private ArrayList<Hashtag> hashtags = new ArrayList<>();
	
	public Tweet(String createdAt, String text, User user, String location) {
		this.createdAt = createdAt;
		this.text = text;
		this.user = user;
		this.location = location;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	public String getText() {
		return text;
	}
	public User getUser() {
		return user;
	}
	public String getLocation() {
		return location;
	}
	public URL getProfileImage() {
		return profileImage;
	}
	
	public ArrayList<Hashtag> getHashTags() {
		return hashtags;
	}
	
	public void addHashTag(Hashtag hashTag) {
		hashtags.add(hashTag);
	}
	

	
}
