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
					
/**
 * AsyncTask voor het ophalen van tweets bij een zoekopdracht
 * @author Ricardo
 *
 */
public class TaskGetTweets extends AsyncTask<String, Double, JSONObject> {
	private TwitterModel model;
	private TokenManager manager;
	
	public TaskGetTweets(TokenManager manager) {
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
		
		//Controleert of de gebruiker is ingelogd. Als dit het geval is wordt de acces token gebruikt.
		//Anders wordt de bearer token gebruikt.
		if(manager.isLoggedIn()){
			try {
				manager.signWithUserToken(httpGet);
			} catch (OAuthException e1) {
				Log.d("Request signen", "Signen mislukt");
			}
		}
		else{
			httpGet.setHeader("Authorization", "Bearer " + manager.bearerToken);
		}
		
		
		ResponseHandler<String> handler = new BasicResponseHandler();
		String result = "";
		
		try {
			result = client.execute(httpGet, handler);
		} catch (ClientProtocolException e) {
			Log.d("Request uitvoeren", "Uitvoeren mislukt");
		} catch (IOException e) {
			Log.d("Request uitvoeren", "Uitvoeren mislukt");
		}
		
		JSONObject jason = null;
		try {
			jason = new JSONObject(result);
		} catch (JSONException e) {
			Log.d("JSON parsen uit tweets", "Omvormen kan niet");
		}
		
		return jason;
	}
}
