package nl.saxion.rn.projecttwitterclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.rn.projecttwitterclient.model.User;
import oauth.signpost.exception.OAuthException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CommunicateToTwitter.TaskGetTweets;
import CommunicateToTwitter.TokenManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	private ListView list;
	private TweetAdapter adapter;
	private TwitterModel model;
	private JSONObject searchResult;
	private TokenManager tokenManager;
	private String screen_name;

	/**
	 * Hier worden alle user interface-componenten en onClickListeners gedeclareerd
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = (ListView)findViewById(R.id.listViewSearch);
		Button buttonStartSearch = (Button) findViewById(R.id.buttonStartSearch);
		final EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		
		TwitterApplication app = (TwitterApplication) getApplicationContext();
		
		model = app.getModel();
		tokenManager = app.getManager();
		
		Log.d("Bearer token", tokenManager.bearerToken);
		Log.d("Token manager", tokenManager.bearerToken);
		
		adapter = new TweetAdapter(this, R.layout.tweet, model.getTweets());
		list.setAdapter(adapter);
		
		buttonStartSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskGetTweets task = new TaskGetTweets(tokenManager);
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
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				User user = adapter.getItem(position).getUser();
				if(user.getScreenName().length() > 0){
					screen_name = user.getScreenName();
				}
				else{
					Log.d("Screen Name", "Geen screen name");
				}
				
				AlertDialog alertDialog = new AlertDialog.Builder(SearchActivity.this).create();
				alertDialog.setTitle("Vriend toevoegen");
				alertDialog.setMessage("Wilt u deze gebruiker toevoegen als vriend");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ja", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(tokenManager.isLoggedIn()){
							new CreateFriendship().execute(screen_name);
						}
						else{
							Toast.makeText(getApplicationContext(), "U moet ingelogd zijn om iemand te volgen", Toast.LENGTH_LONG).show();
						}
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nee", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				alertDialog.show();
				
				return true;
			}
		});
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
	 * Parsed de ontvangen tweets om naar classes om zo in de listView weer te geven
	 */
	public void parseJSON(JSONObject statuses) {
		try{
			model.getTweets().clear();
			model.getUsers().clear();
			Log.d("JSON inlezen", "Geslaagd");
			
			JSONArray statusArray = statuses.getJSONArray("statuses");
			
			for(int i = 0; i < statusArray.length(); i++) {
				
				Tweet tweetToAdd = new Tweet(statusArray.getJSONObject(i));
				
				if(tweetToAdd != null){
					model.addTweet(tweetToAdd);
					model.addUser(tweetToAdd.getUser());
				}
				Log.d("Tweets", String.valueOf(model.getTweets().size()));
			}
		}
		catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Couldn't find data", Toast.LENGTH_LONG).show();
			Log.d("JSON uitlezen", "Fout");
		}
	}
	
	/**
	 * AsyncTask voor het toevoegen van een vriend
	 * @author Ricardo
	 *
	 */
	private class CreateFriendship extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d("Gebruik volgen", params[0]);
			
			String encodedUser = "";
			try {
				encodedUser = URLEncoder.encode(params[0], "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				Log.d("Gebruiker coderen", "Coderen mislukt");
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/friendships/create.json?screen_name=" + encodedUser + "&follow=true");
			
			try {
				tokenManager.signWithUserToken(httpPost);
			} catch (OAuthException e1) {
				Toast.makeText(getApplicationContext(), "U moet ingelogd zijn om iemand te volgen", Toast.LENGTH_SHORT).show();
			}
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpPost, handler);
			} catch (ClientProtocolException e) {
				Log.d("Request uitvoeren", "Uitvoeren mislukt");
			} catch (IOException e) {
				Log.d("Request uitvoeren", "Uitvoeren mislukt");
			}
			
			if(result.length() > 0){
				return params[0];
			}
			else{
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
			Toast.makeText(getApplicationContext(), "U volgt nu " + result, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
