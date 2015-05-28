package nl.saxion.rn.projecttwitterclient;

import java.net.URL;

public class User {
	private String createdAt, description, location, name;
	private int followersCount, friendsCount;
	private URL profileImage;
	
	public User(String createdAt, String description, String location, String name, int followersCount, int friendsCount) {
		this.createdAt = createdAt;
		this.description = description;
		this.location = location;
		this.name = name;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
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

	public URL getProfileImage() {
		return profileImage;
	}

}
