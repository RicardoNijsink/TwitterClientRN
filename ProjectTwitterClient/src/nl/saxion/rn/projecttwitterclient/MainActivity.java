package nl.saxion.rn.projecttwitterclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nl.rn.projecttwitterclient.model.HashTag;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.rn.projecttwitterclient.model.URL;
import nl.rn.projecttwitterclient.model.User;
import nl.rn.projecttwitterclient.model.UserMention;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView list;
	private TweetAdapter adapter;
	private TwitterModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = (ListView)findViewById(R.id.listView1);
		TwitterApplication app = (TwitterApplication) getApplicationContext();
		
		model = app.getModel();

		parseJSON();
		
		adapter = new TweetAdapter(this, R.layout.tweet, model.getTweets());
		//model.addObserver(adapter);
		list.setAdapter(adapter);
		
	}
	
 	/**
     * Reads an asset file and returns a string with the full contents.
     *
     * @param filename  The filename of the file to read.
     * @return          The contents of the file.
     * @throws IOException  If file could not be found or not read.
     */
    private String readAssetIntoString(String filename) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
			InputStream is = getAssets().open(filename, AssetManager.ACCESS_BUFFER);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
            throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();	
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Parses a JSON-file to classes.
	 * Gets the tweets and users and adds them to the model.
	 *  
	 */
	public void parseJSON() {
		try{
			String result = readAssetIntoString("searchresult.json");
			Log.d("JSON inlezen", "Geslaagd");
			
			JSONObject statuses = new JSONObject(result);
			JSONArray statusArray = statuses.getJSONArray("statuses");
			
			for(int i = 0; i < statusArray.length(); i++) {
				JSONObject tweet = statusArray.getJSONObject(i);
				String createdAt = tweet.getString("created_at");
				String text = tweet.getString("text");
				
				JSONObject user = new JSONObject(tweet.getString("user"));
				String name = user.getString("name");
				String location = user.getString("location");
				String description = user.getString("description");
				int followersCount = user.getInt("followers_count");
				int friendsCount = user.getInt("friends_count");
				String profileImageURL = user.getString("profile_image_url");
				
				User userToAdd = new User(createdAt, description, location, name, followersCount, friendsCount);
				userToAdd.setProfileImage(profileImageURL);
				model.addUser(userToAdd);
				
				Tweet tweetToAdd = new Tweet(createdAt, text, userToAdd, location);
				
				JSONObject entities = new JSONObject(tweet.getString("entities"));
				JSONArray hashTags = entities.getJSONArray("hashtags");
				
				if(hashTags.length() != 0){
					for(int counter = 0; counter < hashTags.length(); counter++){
						JSONObject hashTag = hashTags.getJSONObject(counter);
						JSONArray hashtagIndices = hashTag.getJSONArray("indices");
						
						int hashtagBegin = hashtagIndices.getInt(0);
						int hashtagEnd = hashtagIndices.getInt(1);
						
						HashTag hashTagToAdd = new HashTag(hashtagBegin, hashtagEnd);
						tweetToAdd.addHashTag(hashTagToAdd);
					}
				}
				
				try{
					JSONArray urls = entities.getJSONArray("media");
					
					if(urls.length() != 0){
						for(int urlcounter = 0; urlcounter < urls.length(); urlcounter++){
							JSONObject url = urls.getJSONObject(urlcounter);
							JSONArray urlIndices = url.getJSONArray("indices");
							
							int urlBegin = urlIndices.getInt(0);
							int urlEnd = urlIndices.getInt(1);
							
							URL urlToAdd = new URL(urlBegin, urlEnd);
							tweetToAdd.addURL(urlToAdd);
						}
					}
				}
				catch(Exception e){
					Log.d("URL", "Geen URL");
				}
				
				try{
					JSONArray userMentions = entities.getJSONArray("user_mentions");
					
					if(userMentions.length() != 0){
						for(int userMentionsCounter = 0; userMentionsCounter < userMentions.length(); userMentionsCounter++){
							JSONObject userMention = userMentions.getJSONObject(userMentionsCounter);
							JSONArray userMentionIndices = userMention.getJSONArray("indices");
							
							int userMentionBegin = userMentionIndices.getInt(0);
							int userMentionEnd = userMentionIndices.getInt(1);
							
							UserMention userMentionToAdd = new UserMention(userMentionBegin, userMentionEnd);
							tweetToAdd.addUserMention(userMentionToAdd);
						}
					}
				}
				catch(Exception e){
					Log.d("UserMention", "Geen User Mention");
				}
				
				model.addTweet(tweetToAdd);
			}
		}
		catch (IOException e) {
			Toast toast = new Toast(getApplicationContext());
			toast.setText("Couldn't find data");
			toast.show();
			Log.d("JSON uitlezen", "Fout");
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
