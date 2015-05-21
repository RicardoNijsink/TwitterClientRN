package nl.saxion.rn.projecttwitterclient;

import nl.rn.projecttwitterclient.model.TwitterModel;
import android.app.Application;

public class TwitterApplication extends Application {
	private TwitterModel twitterModel;
	
	@Override
	public void onCreate() {
		twitterModel = new TwitterModel();
	}
	
	public TwitterModel getModel() {
		return twitterModel;
	}

}
