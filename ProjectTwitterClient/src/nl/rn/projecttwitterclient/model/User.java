package nl.rn.projecttwitterclient.model;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	private String createdAt, description, location, name, screen_name;
	private int followersCount, friendsCount;
	private String profileImageURL;
	
	public User(String createdAt, String description, String location, String name, int followersCount, int friendsCount) {
		this.createdAt = createdAt;
		this.description = description;
		this.location = location;
		this.name = name;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
	}
	
	public User(JSONObject user) {
		try{
			name = user.getString("name");
			location = user.getString("location");
			description = user.getString("description");
			followersCount = user.getInt("followers_count");
			friendsCount = user.getInt("friends_count");
			profileImageURL = user.getString("profile_image_url");
			screen_name = user.getString("screen_name");
		}
		catch(JSONException e){
			Log.d("User parsen", "Mislukt");
		}
	}

	public void setProfileImage(String profileImage) {
		this.profileImageURL = profileImage;
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

	public String getProfileImageURL() {
		return profileImageURL;
	}
	
	public String getScreenName() {
		return screen_name;
	}

}
