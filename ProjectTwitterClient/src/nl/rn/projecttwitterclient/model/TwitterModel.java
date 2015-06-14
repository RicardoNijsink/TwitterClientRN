package nl.rn.projecttwitterclient.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TwitterModel extends Observable implements Observer{
	private ArrayList<Tweet> tweets = new ArrayList<>();
	private ArrayList<User> users = new ArrayList<>();
	public JSON
	
	public void addUser(User u) {
		users.add(u);
	}
	
	public void addTweet(Tweet t) {
		tweets.add(t);
	}
	
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
