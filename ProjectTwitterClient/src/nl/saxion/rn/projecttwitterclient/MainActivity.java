package nl.saxion.rn.projecttwitterclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nl.rn.projecttwitterclient.model.HashTag;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

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

		try{
			String result = readAssetIntoString("searchresult.json");
			Log.d("Test", "Geslaagd");
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
				
				Tweet tweetToAdd = new Tweet(createdAt, text, name, location);
				
				JSONObject entities = new JSONObject(tweet.getString("entities"));
				JSONArray hashTags = entities.getJSONArray("hashtags");
				
//				for(int counter = 0; counter < hashTags.length(); counter++){
//					JSONObject hashTag = hashTags.getJSONObject(i);
//					JSONArray indices = hashTag.getJSONArray("indices");
//					
//					int begin = indices.getInt(0);
//					int end = indices.getInt(1);
//					
//					HashTag hashTagToAdd = new HashTag(begin, end);
//					tweetToAdd.addHashTag(hashTagToAdd);
//				}
				
				model.addTweet(tweetToAdd);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			Log.d("Test", "Fout");
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
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
}
