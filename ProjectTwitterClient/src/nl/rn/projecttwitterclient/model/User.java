package nl.rn.projecttwitterclient.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	private String description, location, name, screen_name;
	private int followersCount, friendsCount;
	private String profileImageURL;
	
	/**
	 * De constructor van een User.
	 * Vormt het opgegeven JSONObject om naar een User.
	 * @param user Het om te vormen JSONObject
	 */
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
