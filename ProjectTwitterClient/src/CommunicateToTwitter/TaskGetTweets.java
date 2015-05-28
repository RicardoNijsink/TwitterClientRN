package CommunicateToTwitter;

import org.json.JSONObject;

import android.os.AsyncTask;
											//params progress result
public class TaskGetTweets extends AsyncTask<Object, Object, Object> {

	@Override
	protected JSONObject doInBackground(Object... params) {
		
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
