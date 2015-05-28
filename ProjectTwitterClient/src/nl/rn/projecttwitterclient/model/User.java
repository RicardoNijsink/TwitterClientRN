package nl.rn.projecttwitterclient.model;

import java.net.URL;

public class User {
	private String createdAt, description, location, name;
	private int followersCount, friendsCount;
	private String profileImage;
	
	public User(String createdAt, String description, String location, String name, int followersCount, int friendsCount) {
		this.createdAt = createdAt;
		this.description = description;
		this.location = location;
		this.name = name;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getDescription() {
		return description;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public String getProfileImage() {
		return profileImage;
	}

}
