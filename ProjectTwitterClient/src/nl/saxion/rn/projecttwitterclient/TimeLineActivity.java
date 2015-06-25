package nl.saxion.rn.projecttwitterclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.User;
import oauth.signpost.exception.OAuthException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CommunicateToTwitter.BearerTokenManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeLineActivity extends Activity {
	private BearerTokenManager manager;
	private ArrayList<Tweet> timeLineTweets = new ArrayList<>();
	private TweetAdapter adapter;
	private TextView textViewTimeLineName, textViewTimeLineDescription, textViewTimeLineCreatedAt,
	textViewTimeLineLocation, listViewTimeLineTweets;
	private EditText editTextTweetTimeLine;
	private Button buttonTweetTimeLine, buttonFriendsTimeLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		
		TwitterApplication app = (TwitterApplication)getApplicationContext();
		manager = app.getManager();
		
		textViewTimeLineName = (TextView)findViewById(R.id.textViewTimeLineName);
		textViewTimeLineDescription = (TextView)findViewById(R.id.textViewTimeLineDescription);
		textViewTimeLineCreatedAt = (TextView)findViewById(R.id.textViewTimeLineCreatedAt);
		textViewTimeLineLocation = (TextView)findViewById(R.id.textViewTimeLineLocation);
		editTextTweetTimeLine = (EditText)findViewById(R.id.editTextTweetTimeLine);
		buttonTweetTimeLine = (Button)findViewById(R.id.buttonTweetTimeLine);
		buttonFriendsTimeLine = (Button)findViewById(R.id.buttonFollowers);
	
		ListView listViewTimeLineTweets = (ListView)findViewById(R.id.listViewTimeLineTweets);
		
		adapter = new TweetAdapter(getApplicationContext(), R.layout.tweet, new ArrayList<Tweet>());
		
		new GetUserInfo().execute();
		new GetTimeLine().execute();
		
		listViewTimeLineTweets.setAdapter(adapter);
		
		buttonTweetTimeLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(editTextTweetTimeLine.getText().length() > 1){
					String tweet = String.valueOf(editTextTweetTimeLine.getText());
					new PostTweet().execute(tweet);
				}
				else{
					Toast.makeText(getApplicationContext(), "U moet een geldige tweet posten", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		buttonFriendsTimeLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
			    builder.setTitle("Vrienden")
			           .setItems(TODO, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			               // The 'which' argument contains the index position
			               // of the selected item
			           }
			    });
			    builder.create();
			}
		});
	}
	
	/**
	 * Gets the information of the user that's signed in
	 * @author Ricardo
	 *
	 */
	private class GetUserInfo extends AsyncTask<Void, Void, User> {

		@Override
		protected User doInBackground(Void... params) {
			if(params == null){
				return null;
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/account/verify_credentials.json");
			
			try {
				manager.signWithUserToken(httpGet);
			} catch (OAuthException e1) {
				Log.d("Signing", "Signing failed");
			}
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpGet, handler);
				Log.d("Result time line", result);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			User userToDisplay = null;
			try {
				JSONObject user = new JSONObject(result);
				userToDisplay = new User(user);
				
				Log.d("userToDisplay", userToDisplay.getName());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return userToDisplay;
		}
		
		@Override
		protected void onPostExecute(User result) {
			Log.d("User", result.toString());
			
			if(result != null){
				textViewTimeLineName.setText(result.getName());
				textViewTimeLineName.setVisibility(View.VISIBLE);
				textViewTimeLineDescription.setText(result.getDescription());
				textViewTimeLineDescription.setVisibility(View.VISIBLE);
				textViewTimeLineCreatedAt.setText(result.getCreatedAt());
				textViewTimeLineCreatedAt.setVisibility(View.VISIBLE);
				textViewTimeLineLocation.setText(result.getLocation());
				textViewTimeLineLocation.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	
	private class GetTimeLine extends AsyncTask<Void, Void, ArrayList<Tweet>> {

		@Override
		protected ArrayList<Tweet> doInBackground(Void... params) {
			
			if(params == null){
				return null;
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/statuses/home_timeline.json?count=80");
			
			try {
				manager.signWithUserToken(httpGet);
			} catch (OAuthException e1) {
				Log.d("Signing", "Signing failed");
			}
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpGet, handler);
				Log.d("Result time line", result);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<Tweet> tweetsFromGet = new ArrayList<>();
			try {
				JSONArray timeLineArray = new JSONArray(result);
				
				for(int i = 0; i < timeLineArray.length(); i++) {
					Tweet tweetToAdd = new Tweet(timeLineArray.getJSONObject(i));
					Log.d("Tweet", tweetToAdd.getText());
					
					if(tweetToAdd != null){
						tweetsFromGet.add(tweetToAdd);
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return tweetsFromGet;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Tweet> result) {
			adapter.addAll(result);
			Log.d("Result Post", result.get(0).getText());
			adapter = new TweetAdapter(getApplicationContext(), R.layout.tweet, result);
		}
		
	}
	
	private class PostTweet extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			if(params == null) {
				return null;
			}
			
			String postTweet = "";
			for(int i = 0; i < params.length; i++) {
				postTweet = postTweet + params[i];
			}
			
			String encodedPost = "";
			
			try {
				encodedPost = URLEncoder.encode(postTweet, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status=" + encodedPost);
			
			try {
				manager.signWithUserToken(httpPost);
			} catch (OAuthException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpPost, handler);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(TimeLineActivity.this, TimeLineActivity.class);
			startActivity(intent);
		}
		
	}
	
	private class GetFriendsList extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.action_hoofdmenu) {
			Intent intent = new Intent(TimeLineActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
