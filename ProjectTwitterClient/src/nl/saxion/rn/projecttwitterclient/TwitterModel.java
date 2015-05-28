package nl.saxion.rn.projecttwitterclient;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TwitterModel extends Observable implements Observer{
	private ArrayList<Tweet> tweets;
	
	public void addTweet(Tweet t) {
		tweets.add(t);
	}
	
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
