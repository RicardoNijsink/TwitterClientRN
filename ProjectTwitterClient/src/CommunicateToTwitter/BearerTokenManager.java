package CommunicateToTwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.saxion.rn.projecttwitterclient.TwitterApplication;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class BearerTokenManager {

	private static final String API_KEY = "cvTfgu6ARayg6Ly4oMPNe53cu";
	private static final String API_SECRET = "qlouhSjduvfb9EAqz9iiCUQfgnBG0fCVsxTJX5q9S5HLzBwEvh";
	private TwitterModel model;
	public String bearerToken = "";
	
	public BearerTokenManager(TwitterModel model){
		this.model = model;
		try {
			this.bearerToken = new getBearerToken().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class getBearerToken extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			//Plak key en secret aan elkaar en zorg dat er nooit ‘rare’ tekens inzitten	
			String authString = API_KEY	+ ":" + API_SECRET;
			String base64 = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
			
			//Maak een POST-request	
			HttpPost request = new HttpPost("https://api.twitter.com/oauth2/token");
			
			//Voeg key+secret toe	
			request.setHeader("Authorization", "Basic "	+ base64);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			//Vul body: vertel dat je inlogt met app key en secret	
			try {
				request.setEntity(new StringEntity("grant_type=client_credentials"));
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
			
			HttpClient client = new DefaultHttpClient();
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			String result = "";
			try {
				result = client.execute(request, handler);
				Log.d("Result", result);
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			//result omzetten naar JSONObject
			JSONObject json = null;
			try {
				json = new JSONObject(result);
				Log.d("JSON", json.toString(1));
			} catch (JSONException e) {

				Log.d("Bearer token", "Omvormen kan niet");
				e.printStackTrace();
			}
			
			//token uit JSONObject halen en in model zetten
			String bearerToken = "";
			try {
				bearerToken = json.getString("access_token");
				if(bearerToken != null){
					model.bearerToken = bearerToken;
				}
				else{
					Log.d("Bearer token", "Geen bearer token");
				}
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			return bearerToken;
		}
	}
}
