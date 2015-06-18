package nl.saxion.rn.projecttwitterclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import nl.rn.projecttwitterclient.model.HashTag;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.rn.projecttwitterclient.model.URL;
import nl.rn.projecttwitterclient.model.User;
import nl.rn.projecttwitterclient.model.UserMention;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CommunicateToTwitter.TaskGetTweets;
import CommunicateToTwitter.BearerTokenManager;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView list;
	private TweetAdapter adapter;
	private TwitterModel model;
	private JSONObject searchResult;
	private BearerTokenManager bearerTokenManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = (ListView)findViewById(R.id.listView1);
		Button buttonStartSearch = (Button) findViewById(R.id.buttonStartSearch);
		final EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		
		TwitterApplication app = (TwitterApplication) getApplicationContext();
		
		model = app.getModel();
		bearerTokenManager = app.getManager();
		
		Log.d("Bearer token", model.bearerToken);
		Log.d("Bearer token manager", bearerTokenManager.bearerToken);
		
		adapter = new TweetAdapter(this, R.layout.tweet, model.getTweets());
		//model.addObserver(adapter);
		list.setAdapter(adapter);
		
		buttonStartSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskGetTweets task = new TaskGetTweets(model);
				try {
					searchResult = task.execute(editTextSearch.getText().toString()).get();
					parseJSON(searchResult);
					adapter.notifyDataSetChanged();
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
			}
			
		});
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
	public void parseJSON(JSONObject statuses) {
		try{
			
//			String result = readAssetIntoString("searchresult.json");
			Log.d("JSON inlezen", "Geslaagd");
			
			//JSONObject statuses = new JSONObject(result);
			JSONArray statusArray = statuses.getJSONArray("statuses");
			
			for(int i = 0; i < statusArray.length(); i++) {
				JSONObject tweet = statusArray.getJSONObject(i);
				
				Tweet tweetToAdd = new Tweet(statusArray.getJSONObject(i));
				
				if(tweetToAdd != null){
					model.addTweet(tweetToAdd);
					model.addUser(tweetToAdd.getUser());
				}
				Log.d("Tweets", String.valueOf(model.getTweets().size()));
			}
		}
//		catch (IOException e) {
//			Toast toast = new Toast(getApplicationContext());
//			toast.setText("Couldn't find data");
//			toast.show();
//			Log.d("JSON uitlezen", "Fout");
//		}
		catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Couldn't find data", Toast.LENGTH_LONG).show();
			Log.d("JSON uitlezen", "Fout");
		}
	}
}
