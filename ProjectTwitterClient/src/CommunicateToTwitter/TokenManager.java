package CommunicateToTwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.saxion.rn.projecttwitterclient.TwitterApplication;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class TokenManager {

	private static final String API_KEY = "cvTfgu6ARayg6Ly4oMPNe53cu";
	private static final String API_SECRET = "qlouhSjduvfb9EAqz9iiCUQfgnBG0fCVsxTJX5q9S5HLzBwEvh";
	private static final String OAUTH_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	private static final String OAUTH_ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	private static final String OAUTH_AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	public static final String CALL_BACK_URL = "https://apps.twitter.com/app/new";
	private static String oauthVerifier = "";

	private TwitterModel model;
	private boolean loggedIn = false;
	public String bearerToken = "";
	public String url = "";
	
	private static CommonsHttpOAuthConsumer oAuthConsumer;
	private static CommonsHttpOAuthProvider oAuthProvider;
	
	public TokenManager(TwitterModel model){
		init();
		
		this.model = model;
		new getBearerToken().execute();
	}
	
	private void init() {
		oAuthConsumer = new CommonsHttpOAuthConsumer(API_KEY, API_SECRET);
		oAuthProvider = new CommonsHttpOAuthProvider(OAUTH_REQUEST_URL, OAUTH_ACCESS_URL, OAUTH_AUTHORIZE_URL);
	}
	
	public void retreiveAccessToken() {
		new RetreiveAccessToken().execute();
	}
	
	/**
	 * AsyncTask voor het ophalen van de bearer token
	 * Als de AsyncTask klaar is, wordt de bearer token aan de manager toegevoegd
	 * @author Ricardo
	 *
	 */
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
			
			//Vul body: vertelt dat je inlogt met app key en secret	
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
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			return bearerToken;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null && result.length() > 0){
				bearerToken = result;
			}
		}
	}
	
	/**
	 * AsyncTask voor het ophalen van de request token
	 * Als de AsyncTask klaar is, wordt de request token terug gegeven.
	 * @author Ricardo
	 *
	 */
	public static class GetRequestToken extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String url = "";
			
			try {
				url = oAuthProvider.retrieveRequestToken(oAuthConsumer, CALL_BACK_URL);
			} catch (OAuthMessageSignerException | OAuthNotAuthorizedException
					| OAuthExpectationFailedException
					| OAuthCommunicationException e) {
				Log.d("Request token", "Ophalen mislukt");
			}
			
			return url;
		}
	}
	
	public static class RetreiveAccessToken extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				oAuthProvider.retrieveAccessToken(oAuthConsumer, oauthVerifier);
				Log.d("Access token", oAuthConsumer.getToken());
			} catch (OAuthMessageSignerException e) {
				Log.d("Acces token", "Ophalen mislukt");
			} catch (OAuthNotAuthorizedException e) {
				Log.d("Acces token", "Ophalen mislukt");
			} catch (OAuthExpectationFailedException e) {
				Log.d("Acces token", "Ophalen mislukt");
			} catch (OAuthCommunicationException e) {
				Log.d("Acces token", "Ophalen mislukt");
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			String token = oAuthConsumer.getToken();
			String token_secret = oAuthConsumer.getTokenSecret();
			
			//TODO sharedPreferences
		}
	}
	
	/**
	 * Signed de request met de acces token, zodat de applicatie toestemming heeft om de request uit te voeren
	 * @param request De te signen request
	 * @return True, als de request gesigned is, anders False
	 * @throws OAuthException
	 */
	public boolean signWithUserToken(HttpRequestBase request) throws OAuthException {
		if(isLoggedIn()){	
			oAuthConsumer.sign(request);
			return true;
		}
		else{
			return false;
		}
	}	

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public static String getCallBackUrl() {
		return CALL_BACK_URL;
	}
	
	public void setOauthVerifier(String oauth_verifier) {
		this.oauthVerifier = oauth_verifier;
	}
}
