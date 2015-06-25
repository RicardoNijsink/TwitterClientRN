package CommunicateToTwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.saxion.rn.projecttwitterclient.TwitterApplication;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
											//params progress result
public class TaskGetTweets extends AsyncTask<String, Double, JSONObject> {
	private TwitterModel model;
	private BearerTokenManager manager;
	
	public TaskGetTweets(BearerTokenManager manager) {
		this.manager = manager;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		
		if(params == null) {
			return null;
		}
		
		String search = "";
		for(int i = 0; i < params.length; i++) {
			search = search + params[i];
		}
		
		String encodedSearch = "";
		
		try {
			encodedSearch = URLEncoder.encode(search, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/search/tweets.json?q=" + encodedSearch);
		
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
		
		//loop tot het klaar is
		//publishProgress();
		
		//je ontvangt een String van twitter
		
		//deel om de String naar een JSONObject te parsen
		JSONObject jason = null;
		try {
			jason = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSON parsen uit tweets", "Omvormen kan niet");
			e.printStackTrace();
		}
		
		return jason;//returned het JSONObject
	}
	
	protected void onProgressUpdate(Double... percent) {
		//iets om de voortgang weer te geven
		//zet hoeveel gedownload/TOTAAL in een label ofzo via een methode in de scherm activity
	}
}
