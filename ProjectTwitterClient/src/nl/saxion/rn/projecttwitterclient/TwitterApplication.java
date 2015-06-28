package nl.saxion.rn.projecttwitterclient;

import nl.rn.projecttwitterclient.model.TwitterModel;
import CommunicateToTwitter.TokenManager;
import android.app.Application;

public class TwitterApplication extends Application {
	private TwitterModel twitterModel;
	private TokenManager manager;
	
	/**
	 * Hier worden het model en de manager aangemaakt
	 */
	@Override
	public void onCreate() {
		twitterModel = new TwitterModel();
		manager = new TokenManager(twitterModel);
	}
	
	public TwitterModel getModel() {
		return twitterModel;
	}
	
	public TokenManager getManager() {
		return manager;
	}

}
