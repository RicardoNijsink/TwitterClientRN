package nl.saxion.rn.projecttwitterclient;

import java.io.IOException;

import oauth.signpost.exception.OAuthException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import CommunicateToTwitter.BearerTokenManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TimeLineActivity extends Activity {
	private BearerTokenManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		
		TwitterApplication app = (TwitterApplication)getApplicationContext();
		manager = app.getManager();
		
		TextView textViewTimeLineName = (TextView)findViewById(R.id.textViewTimeLineName);
		TextView textViewTimeLineDescription = (TextView)findViewById(R.id.textViewTimeLineDescription);
		TextView textViewTimeLineCreatedAt = (TextView)findViewById(R.id.textViewTimeLineCreatedAt);
		TextView textViewTimeLineLocation = (TextView)findViewById(R.id.textViewTimeLineLocation);
		
		
		
	}
	
	private class GetTimeLine extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			if(params == null){
				return null;
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/statuses/home_timeline.json");
			
			//httpGet.setHeader("Authorization", "Bearer " + model.bearerToken);
			try {
				manager.signWithUserToken(httpGet);
			} catch (OAuthException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Log.d("Bearer token request", model.bearerToken);
			
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			String result = "";
			
			try {
				result = client.execute(httpGet, handler);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
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
