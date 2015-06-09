package CommunicateToTwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
											//params progress result
public class TaskGetTweets extends AsyncTask<String, Double, JSONObject> {

	@Override
	protected JSONObject doInBackground(String... params) {
		
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
		HttpGet httpGet = new HttpGet(encodedSearch);
		
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
		
		return null;//returned de JSONObject
	}
	
	protected void onProgressUpdate(Double... percent) {
		//iets om de voortgang weer te geven
	}



}
