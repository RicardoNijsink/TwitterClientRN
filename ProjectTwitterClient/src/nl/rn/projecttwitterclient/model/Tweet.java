package nl.rn.projecttwitterclient.model;

import java.net.URL;
import java.util.ArrayList;

import nl.saxion.rn.projecttwitterclient.HashTag;
import nl.saxion.rn.projecttwitterclient.User;

public class Tweet {
	private String createdAt, text, location;
	private User user;
	private URL profileImage;
	private ArrayList<HashTag> hashTags = new ArrayList<>();
	
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
	
	public ArrayList<HashTag> getHashTags() {
		return hashTags;
	}
	
	public void addHashTag(HashTag hashTag) {
		hashTags.add(hashTag);
	}
	

	
}
