package nl.rn.projecttwitterclient.model;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet {
	private String createdAt, text, location;
	private User user;
	private java.net.URL profileImage;
	private ArrayList<HashTag> hashTagsList = new ArrayList<>();
	private ArrayList<URL> urlsList = new ArrayList<>();
	private ArrayList<UserMention> userMentionsList = new ArrayList<>();
	
	public Tweet(String createdAt, String text, User user, String location) {
		this.createdAt = createdAt;
		this.text = text;
		this.user = user;
		this.location = location;
	}
	
	public Tweet(JSONObject tweet) {
		try{
			createdAt = tweet.getString("created_at");
			text = tweet.getString("text");
			
			try{
				JSONObject entities = new JSONObject(tweet.getString("entities"));
				JSONArray hashTags = entities.getJSONArray("hashtags");
				
				if(hashTags.length() != 0){
					for(int counter = 0; counter < hashTags.length(); counter++){
							HashTag hashTag = new HashTag(hashTags.getJSONObject(counter));
							hashTagsList.add(hashTag);
					}
				}
				
				JSONArray urls = entities.getJSONArray("media");
				
				if(urls.length() != 0){
					for(int urlcounter = 0; urlcounter < urls.length(); urlcounter++){
						try{
							URL url = new URL(urls.getJSONObject(urlcounter));
							urlsList.add(url);
						}
						catch(JSONException e){
							Log.d("URL", "URL Mislukt");
						}	
					}
				}
				
				JSONArray userMentions = entities.getJSONArray("user_mentions");
				
				if(userMentions.length() != 0){
					for(int userMentionsCounter = 0; userMentionsCounter < userMentions.length(); userMentionsCounter++){
						try{
							UserMention userMention = new UserMention(userMentions.getJSONObject(userMentionsCounter));
							userMentionsList.add(userMention);
						}
						catch(JSONException e){
							Log.d("User Mention", "UM Mislukt");
						}
					}
				}
			}
			catch(JSONException e){
				Log.d("Henk", "Henk");
			}
			
		JSONObject userToAdd = new JSONObject(tweet.getString("user"));
		user = new User(userToAdd);
		Log.d("Test", "Hoi5");
		
		}
		catch(JSONException e){
			Log.d("Tweet parsen", "Mislukt");
		}
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
	public java.net.URL getProfileImage() {
		return profileImage;
	}
	
	public ArrayList<HashTag> getHashTags() {
		return hashTagsList;
	}
	
	public void addHashTag(HashTag hashTag) {
		hashTagsList.add(hashTag);
	}
	
	public ArrayList<URL> getUrls() {
		return urlsList;
	}
	
	public void addURL(URL url) {
		urlsList.add(url);
	}
	
	public ArrayList<UserMention> getUserMentions() {
		return userMentionsList;
	}
	
	public void addUserMention(UserMention userMention) {
		userMentionsList.add(userMention);
	}
	

	
}
