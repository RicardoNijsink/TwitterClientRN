package CommunicateToTwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

import android.util.Base64;
import android.util.Log;

public class getBearerToken {

	private static final String API_KEY = "cvTfgu6ARayg6Ly4oMPNe53cu";
	private static final String API_SECRET = "qlouhSjduvfb9EAqz9iiCUQfgnBG0fCVsxTJX5q9S5HLzBwEvh";
	private TwitterModel model;
	
	public getBearerToken() {
		
		//		Plak	key	en	secret	aan	elkaar	en	zorg	dat	er	nooit	‘rare’	
		//		tekens	inzitten	
		String	authString	=	API_KEY	+	":"	+	API_SECRET;
		String	base64	=	Base64.encodeToString(	
		authString.getBytes(),	Base64.NO_WRAP);	
		//		Maak	een	POST-request	
		HttpPost	request	=		
		new	HttpPost("https://api.twitter.com/oauth2/token");	
		//		Voeg	key+secret	toe	
		request.setHeader("Authorization",	"Basic "	+	base64);	
		request.setHeader("Content-Type",	"application/x-www-form-urlencoded;charset=UTF-8");	
		//		Vul	body:	vertel	dat	je	inlogt	met	app	key	en	secret	
		try {
			request.setEntity(	
			new	StringEntity("grant_type=client_credentials"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpClient client = new DefaultHttpClient();
		
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		String result = null;
		try {
			result = client.execute(request, handler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//result omzetten naar JSONObject
		JSONObject jason = null;
		try {
			jason = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("het json resultaat", "omvormen kan niet");
			e.printStackTrace();
		}
		
		//token uit JSONObject halen en in model zetten
		String bearerToken = "";
		try {
			bearerToken = jason.getString("acces_token");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
