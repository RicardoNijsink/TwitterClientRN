package nl.saxion.rn.projecttwitterclient;

import nl.rn.projecttwitterclient.model.TwitterModel;
import CommunicateToTwitter.BearerTokenManager;
import android.app.Application;

public class TwitterApplication extends Application {
	private TwitterModel twitterModel;
	private BearerTokenManager manager;
	
	@Override
	public void onCreate() {
		twitterModel = new TwitterModel();
		manager = new BearerTokenManager(twitterModel);
	}
	
	public TwitterModel getModel() {
		return twitterModel;
	}
	
	public BearerTokenManager getManager() {
		return manager;
	}

}
