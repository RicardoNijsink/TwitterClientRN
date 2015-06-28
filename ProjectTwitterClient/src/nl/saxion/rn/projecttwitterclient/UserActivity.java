package nl.saxion.rn.projecttwitterclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import oauth.signpost.exception.OAuthException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import com.squareup.picasso.Picasso;

import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.rn.projecttwitterclient.model.User;
import nl.saxion.rn.projecttwitterclient.TimeLineActivity.GetFriendsList;
import CommunicateToTwitter.TokenManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity {
	private TwitterModel model;
	
	private TextView name;
	private EditText editTextDirectMessage;
	private TokenManager manager;
	private User u;
	
	/**
	 * Hier worden alle user interface-componenten en onClickListeners gedeclareerd
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		TwitterApplication app = (TwitterApplication) getApplication();
		model = app.getModel();
		manager = app.getManager();
		
		name = (TextView)findViewById(R.id.textViewName);
		TextView description = (TextView)findViewById(R.id.textViewDescription);
		TextView followersCount = (TextView)findViewById(R.id.textViewFollowers);
		TextView friendsCount = (TextView)findViewById(R.id.textViewFriends);
		ImageView imageViewUserPicture = (ImageView)findViewById(R.id.imageViewUserPicture);
		
		editTextDirectMessage = (EditText) findViewById(R.id.editTextDirectMessage);
		Button buttondDirectMessage = (Button) findViewById(R.id.buttonDirectMessage);
		
		//Hier wordt de meegegeven user opgehaald in de bijbehorende informatie ingevuld
		Intent intent = getIntent();
		int position = intent.getIntExtra("Position", -1);
		String naam = intent.getStringExtra("naam");
		
		if(position >= 0){
		u = model.getUsers().get(position);
		}
		
		if(u == null){
			int naamPositie = model.getUsers().indexOf(naam);
			u = model.getUsers().get(naamPositie);
		}
		
		name.setText("" + u.getName() + "  (@" + u.getScreenName() + ")");
		description.setText("" + u.getDescription());
		followersCount.setText("Volgers: " + u.getFollowersCount());
		friendsCount.setText("Volgend: " + u.getFriendsCount());
		
		Picasso.with(getApplicationContext()).load(u.getProfileImageURL()).into(imageViewUserPicture);

		buttondDirectMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<User> friendList = new ArrayList<>();
				
				try {
					friendList = new GetFriendsList(manager).execute().get();
				} catch (InterruptedException e) {
					Log.d("Vriendenlijst ophalen", "Ophalen mislukt");
				} catch (ExecutionException e) {
					Log.d("Vriendenlijst ophalen", "Ophalen mislukt");
				}
				
				boolean isFriend = false;
				for(User user : friendList){
					if(u.getScreenName().equals(user.getScreenName())){
						new PostDirectMessage().execute(u.getScreenName());
						isFriend = true;
					}
				}
				
				if(!isFriend){
					Toast.makeText(getApplicationContext(), "U bent geen vrienden met deze gebruiker", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	/**
	 * AsyncTask voor het sturen van een direct message
	 * @author Ricardo
	 *
	 */
	private class PostDirectMessage extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			TwitterApplication app = (TwitterApplication)getApplicationContext();
			TokenManager manager = app.getManager();
			
			if(editTextDirectMessage == null || editTextDirectMessage.toString().equals("")) {
				return null;
			} 
			
			String message = editTextDirectMessage.getText().toString();
			String screen_name = params[0];
			
			Log.d("Wat zijn de waarden", message + screen_name);
			
			String codedMessage = "";
			try {
				codedMessage = URLEncoder.encode(message, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				Log.d("Bericht coderen", "Coderen mislukt");
			}
			
			String codedScreenName = "";
			try {
				codedScreenName = URLEncoder.encode(screen_name, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				Log.d("Screen name coderen", "Coderen mislukt");
			}
			
			Log.d("Wat zijn de waarden nu", codedMessage + codedScreenName);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/direct_messages/new.json?text=" + codedMessage + "&screen_name=" + codedScreenName);
			
			try {
				manager.signWithUserToken(httpPost);
			} catch (OAuthException e1) {
				Toast.makeText(getApplicationContext(), "Sign Fout", Toast.LENGTH_LONG).show();
				return null;
			}
			
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpPost, handler);
			} catch (ClientProtocolException e) {
				Log.d("Request direct message uitvoeren", "Uitvoeren mislukt");
				Toast.makeText(getApplicationContext(), "Bericht versturen mislukt", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Log.d("Request direct message uitvoeren", "Uitvoeren mislukt");
				Toast.makeText(getApplicationContext(), "Bericht versturen mislukt", Toast.LENGTH_LONG).show();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null && result.length() > 0){
				Toast.makeText(getApplicationContext(), "Het versturen van het bericht is gelukt.", Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
