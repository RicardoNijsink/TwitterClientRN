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
		} catch(JSONException e){
			Log.d("Created at", "Created at mislukt");
		}
		
		try{
			text = tweet.getString("text");
		} catch(JSONException e){
			Log.d("Text", "Text mislukt");
		}
			
		JSONObject entities = new JSONObject();
		
		try{
			entities = new JSONObject(tweet.getString("entities"));
			Log.d("Entities overzicht", String.valueOf(entities));
		} catch(JSONException e){
			Log.d("Entities", "Entities mislukt");
		}
			
		JSONArray hashTags = new JSONArray();
		
		try{
			hashTags = entities.getJSONArray("hashtags");
		} catch(JSONException e){
			Log.d("Hashtags", "Hashtags mislukt");
		}
		
		if(hashTags.length() != 0){
			for(int counter = 0; counter < hashTags.length(); counter++){
				try{	
					HashTag hashTag = new HashTag(hashTags.getJSONObject(counter));
					hashTagsList.add(hashTag);
				}
				catch(JSONException e){
					Log.d("Hashtag", "Hashtag mislukt");
				}
			}
		}
				
		JSONArray urls = new JSONArray();
				
		try{
			urls = entities.getJSONArray("media");
			Log.d("URL lengte", String.valueOf(urls.length()));
		} catch(JSONException e){
			Log.d("Urls", "Urls mislukt");
		}
		
		if(urls.length() == 0){
			try{
				urls = entities.getJSONArray("urls");
				Log.d("URL lengte", String.valueOf(urls.length()));
			} catch(JSONException e){
				Log.d("Urls", "Urls niet gevonden");
			}
		}
				
		if(urls.length() != 0){
			for(int urlcounter = 0; urlcounter < urls.length(); urlcounter++){
				try{
					URL url = new URL(urls.getJSONObject(urlcounter));
					Log.d("URL pos", String.valueOf(url.getBeginPosition()));
					urlsList.add(url);
				}
				catch(JSONException e){
					Log.d("URL", "URL mislukt");
				}	
			}
		}
				
		JSONArray userMentions = new JSONArray();
				
		try{
			userMentions = entities.getJSONArray("user_mentions");
		} catch(JSONException e){
			Log.d("User mentions", "User mentions mislukt");
		}
				
		if(userMentions.length() != 0){
			for(int userMentionsCounter = 0; userMentionsCounter < userMentions.length(); userMentionsCounter++){
				try{ 	
					UserMention userMention = new UserMention(userMentions.getJSONObject(userMentionsCounter));
					userMentionsList.add(userMention);
					Log.d("User mention list", userMentionsList.toString());
				}
				catch(JSONException e){
					Log.d("User Mention", "UM mislukt");
				}
			}
		}	
				
		try{	
			JSONObject userToAdd = new JSONObject(tweet.getString("user"));
			user = new User(userToAdd);
		} catch(JSONException e){
			Log.d("User", "User mislukt");
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
