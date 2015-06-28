package nl.saxion.rn.projecttwitterclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import nl.rn.projecttwitterclient.model.DirectMessage;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;
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

import CommunicateToTwitter.TokenManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeLineActivity extends Activity {
	private static TokenManager manager;
	private TwitterModel model;
	private ArrayList<Tweet> timeLineTweets = new ArrayList<>();
	private TweetAdapter adapter;
	private static String[] friendsList;
	private String[] directMessages = new String[10];
	private TextView textViewTimeLineName, textViewTimeLineDescription,	textViewTimeLineLocation, listViewTimeLineTweets;
	private EditText editTextTweetTimeLine;
	private Button buttonTweetTimeLine, buttonFriendsTimeLine, buttonDirectMessagesTimeLine;

	/**
	 * Hier worden alle user interface-componenten en onClickListeners gedeclareerd en alle asyncTasks aangeroepen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		
		TwitterApplication app = (TwitterApplication)getApplicationContext();
		model = app.getModel();
		manager = app.getManager();
		
		textViewTimeLineName = (TextView)findViewById(R.id.textViewTimeLineName);
		textViewTimeLineDescription = (TextView)findViewById(R.id.textViewTimeLineDescription);
		textViewTimeLineLocation = (TextView)findViewById(R.id.textViewTimeLineLocation);
		editTextTweetTimeLine = (EditText)findViewById(R.id.editTextTweetTimeLine);
		buttonTweetTimeLine = (Button)findViewById(R.id.buttonTweetTimeLine);
		buttonFriendsTimeLine = (Button)findViewById(R.id.buttonFollowers);
		buttonDirectMessagesTimeLine = (Button)findViewById(R.id.buttonDirectMessages);
		friendsList = new String[100];

		ListView listViewTimeLineTweets = (ListView)findViewById(R.id.listViewTimeLineTweets);
		
		adapter = new TweetAdapter(getApplicationContext(), R.layout.tweet, new ArrayList<Tweet>());
		
		new GetUserInfo().execute();
		new GetTimeLine().execute();
		new GetDirectMessages().execute();
		new GetFriendsList(manager).execute();
		
		listViewTimeLineTweets.setAdapter(adapter);
		
		buttonTweetTimeLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("Messagessssss", directMessages[1]);
				
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
				String[] friends = new String[10];
				ArrayAdapter<String> adapterArray = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
				
				for(int i = 0; i < friendsList.length; i++){
					if(friendsList[i] != null){
						friends[i] = friendsList[i];
						adapterArray.add(friends[i]);
						Log.d("Friends", friends[i]);
					}
				}
				
				
				AlertDialog.Builder builderDialog = new Builder(getApplicationContext());
				builderDialog.setAdapter(adapterArray, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				
				AlertDialog dialog = builderDialog.create();
				dialog.setTitle("Uw vrienden");
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
				
				dialog.show();
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
				Log.d("Request user info uitvoeren", "Uitvoeren mislukt");
			} catch (IOException e) {
				Log.d("Request user info uitvoeren", "Uitvoeren mislukt");
			}
			
			User userToDisplay = null;
			try {
				JSONObject user = new JSONObject(result);
				userToDisplay = new User(user);
				
				Log.d("userToDisplay", userToDisplay.getName());
			} catch (JSONException e) {
				Log.d("JSON parsen", "Parsen mislukt");
			}
			
			return userToDisplay;
		}
		
		//Hier wordt alle ontvangen informatie in de bijbehorende textviews ingevuld
		@Override
		protected void onPostExecute(User result) {
			if(result != null){
				Log.d("User", result.toString());
				
				textViewTimeLineName.setText(result.getName());
				textViewTimeLineName.setVisibility(View.VISIBLE);
				textViewTimeLineDescription.setText(result.getDescription());
				textViewTimeLineDescription.setVisibility(View.VISIBLE);
				textViewTimeLineLocation.setText(result.getLocation());
				textViewTimeLineLocation.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * AsyncTask voor het ophalen van de timeline van de ingelogde gebruiker
	 * @author Ricardo
	 *
	 */
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
				Log.d("Request timeline uitvoeren ", "Uitvoeren mislukt");
			} catch (IOException e) {
				Log.d("Request timeline uitvoeren ", "Uitvoeren mislukt");
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
			Log.d("JSON parsen timeline", "Parsen mislukt");
			}
			
			return tweetsFromGet;
		}
		
		//Hier worden alle ontvangen tweets en bijbehorende gebruikers aan het model toegevoegd
		@Override
		protected void onPostExecute(ArrayList<Tweet> result) {
			for(Tweet t : result){
				adapter.add(t);
				model.addTweet(t);
				model.addUser(t.getUser());
			}
			
			Log.d("Result Post", result.get(0).getText());
			adapter = new TweetAdapter(getApplicationContext(), R.layout.tweet, result);
		}
	}
	
	/**
	 * AsyncTask voor het posten van een tweet
	 * @author Ricardo
	 *
	 */
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
				Log.d("Tweet coderen", "Coderen mislukt");
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status=" + encodedPost);
			
			try {
				manager.signWithUserToken(httpPost);
			} catch (OAuthException e1) {
				Log.d("Request tweet posten signen", "Signen mislukt");
			}
			
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpPost, handler);
			} catch (ClientProtocolException e) {
				Log.d("Request tweet posten uitvoeren ", "Uitvoeren mislukt");
			} catch (IOException e) {
				Log.d("Request tweet posten uitvoeren ", "Uitvoeren mislukt");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(TimeLineActivity.this, TimeLineActivity.class);
			startActivity(intent);
		}
		
	}
	
	/**
	 * AsyncTask voor het ophalen van de vriendenlijst van de ingelogde gebruiker
	 * @author Ricardo
	 *
	 */
	public static class GetFriendsList extends AsyncTask<Void, Void, ArrayList<User>> {
		private TokenManager manager;
		
		public GetFriendsList(TokenManager manager) {
			this.manager = manager;
		}
		
		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/friends/list.json");
			
			try {
				manager.signWithUserToken(httpGet);
			} catch (OAuthException e1) {
				Log.d("Signing", "Signing failed");
			}
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpGet, handler);
				Log.d("Result get friends", result);
			} catch (ClientProtocolException e) {
				Log.d("Request get friends uitvoeren ", "Uitvoeren mislukt");
			} catch (IOException e) {
				Log.d("Request get friends uitvoeren ", "Uitvoeren mislukt");
			}
			
			JSONObject resultObject = new JSONObject();
			try {
				resultObject = new JSONObject(result);
			} catch (JSONException e1) {
				Log.d("JSON friends parsen", "Parsen mislukt");
			}
			
			ArrayList<User> friendsFromGet = new ArrayList<>();
			try {
				JSONArray friendsArray = resultObject.getJSONArray("users");
				
				for(int i = 0; i < friendsArray.length(); i++) {
					User userToAdd = new User(friendsArray.getJSONObject(i));
					Log.d("User", userToAdd.getName());
					
					if(userToAdd != null){
						friendsFromGet.add(userToAdd);
					}
				}
				
			} catch (JSONException e) {
				Log.d("JSONArray friends parsen", "Parsen mislukt");
			}
			
			return friendsFromGet;
		}
		
		@Override
		protected void onPostExecute(ArrayList<User> result) {
			
			if(result != null){
			    for(int i = 0; i < result.size(); i++){
			    	if(result.get(i).getName() != null){
			    		friendsList[i] = result.get(i).getName();
			    		Log.d("User added", result.get(i).getName());
			    	}
			    }
			}
		}
	}
	
	/**
	 * AsyncTask voor het ophalen van alle ontvangen direct messages
	 * @author Ricardo
	 *
	 */
	private class GetDirectMessages extends AsyncTask<Void, Void, ArrayList<DirectMessage>> {

		@Override
		protected ArrayList<DirectMessage> doInBackground(Void... params) {
			if(params == null){
				return null;
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/direct_messages.json?count=10");
			
			try {
				manager.signWithUserToken(httpGet);
			} catch (OAuthException e1) {
				Log.d("Signing", "Signing failed");
			}
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpGet, handler);
				Log.d("Result ", result);
			} catch (ClientProtocolException e) {
				Log.d("Request get direct messages uitvoeren ", "Uitvoeren mislukt");
			} catch (IOException e) {
				Log.d("Request get direct messages uitvoeren ", "Uitvoeren mislukt");
			}
			
			JSONArray directMessagesArray = new JSONArray();
			
			try {
				directMessagesArray = new JSONArray(result);
				Log.d("Array", directMessagesArray.toString());
			} catch (JSONException e) {
				Log.d("JSONArray direct messages parsen", "Parsen mislukt");
			}
			
			ArrayList<DirectMessage> messages = new ArrayList<>();
			
			for(int i = 0; i < directMessagesArray.length(); i++){
				DirectMessage message = null;
				try {
					message = new DirectMessage(directMessagesArray.getJSONObject(i));
					messages.add(message);
					Log.d("Message to add", message.toString());
				} catch (JSONException e) {
					Log.d("JSONArray direct messages parsen", "Parsen mislukt");
				}
			}
			
			return messages;
		}
		
		//Hier worden de direct messages aan de array van de activity toegevoegd en wordt de onClickListener van de directMessages gemaakt
		@Override
		protected void onPostExecute(ArrayList<DirectMessage> result) {
			if(result.size() > 0){
				for(int i = 0; i < result.size(); i++){
					directMessages[i] = result.get(i).toString();
					Log.d("Direct messages", directMessages[i]);
				}
			}
			
			buttonDirectMessagesTimeLine.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String[] messages = new String[10];
					ArrayAdapter<String> adapterArray = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
					
					for(int i = 0; i < directMessages.length; i++){
						if(directMessages[i] != null){
							messages[i] = directMessages[i];
							adapterArray.add(directMessages[i]);
							Log.d("Messages", messages[i]);
						}
					}
					
					
					AlertDialog.Builder builderDialog = new Builder(getApplicationContext());
					builderDialog.setAdapter(adapterArray, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					
					AlertDialog dialog = builderDialog.create();
					dialog.setTitle("Uw ontvangen direct messages");
					dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
					
					dialog.show();
				}
				
			});
			
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
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(TimeLineActivity.this, MenuActivity.class);
		startActivity(intent);
		finish();
	}
}
