package nl.saxion.rn.projecttwitterclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import oauth.signpost.exception.OAuthException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.rn.projecttwitterclient.model.User;
import CommunicateToTwitter.BearerTokenManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity {
	private TwitterModel model;
	
	private TextView name;
	private EditText msgText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		name = (TextView)findViewById(R.id.textViewName);
		TextView description = (TextView)findViewById(R.id.textViewDescription);
		TextView followersCount = (TextView)findViewById(R.id.textViewFollowers);
		TextView friendsCount = (TextView)findViewById(R.id.textViewFriends);
		TextView createdAt = (TextView)findViewById(R.id.textViewCreatedAt);
		
		msgText = (EditText) findViewById(R.id.editTextMsgText);
		Button sendDirectMsg = (Button) findViewById(R.id.buttonDirectMsg);
		
		Intent intent = getIntent();
		int position = intent.getIntExtra("Position", 0);
		
		TwitterApplication app = (TwitterApplication)getApplicationContext();
		model = app.getModel();
		
		User u = model.getUsers().get(position);
		
		name.setText("" + u.getName());
		description.setText("" + u.getDescription());
		createdAt.setText("" + u.getCreatedAt());
		followersCount.setText("Followers: " + u.getFollowersCount());
		friendsCount.setText("Friends: " + u.getFriendsCount());

		sendDirectMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserActivity.this, PostDirectMsg.class);
				startActivity(intent);
			}
			
		});
	}
	
	private class PostDirectMsg extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			TwitterApplication app = (TwitterApplication)getApplicationContext();
			BearerTokenManager manager = app.getManager();
			
			if(msgText == null || msgText.toString().equals("")) {
				return null;
			}
			
			String msgContent = msgText.toString();
			String schermnaamOntvanger = name.toString();
			String userId = name.toString();
			
			try {
				msgContent = URLEncoder.encode(msgContent, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				schermnaamOntvanger = URLEncoder.encode(schermnaamOntvanger, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				userId = URLEncoder.encode(userId, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/direct_messages/new.json?text=" + msgContent + "&screen_name=" + schermnaamOntvanger + "&user_id=" + userId);
			
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
			Toast.makeText(getApplicationContext(), "Het versturen van het bericht is gelukt.", Toast.LENGTH_LONG).show();
			//Intent intent = new Intent(UserActivity.this, TimeLineActivity.class);
			//startActivity(intent);
		}
		
	}
}
