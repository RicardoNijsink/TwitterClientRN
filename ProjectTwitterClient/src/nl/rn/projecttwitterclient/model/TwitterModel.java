package nl.rn.projecttwitterclient.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TwitterModel {
	private ArrayList<Tweet> tweets = new ArrayList<>();
	private ArrayList<User> users = new ArrayList<>();
	
	/**
	 * Voegt een gebruiker toe aan het model
	 * @param u De toe te voegen gebruiker
	 */
	public void addUser(User u) {
		users.add(u);
	}
	
	/**
	 * Voegt een tweet toe aan het model
	 * @param t
	 */
	public void addTweet(Tweet t) {
		tweets.add(t);
	}
	
	/**
	 * Geeft alle tweets uit het model terug 
	 * @return De arrayList met alle tweets uit het model
	 */
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}
	
	/**
	 * Geeft alle users uit het model terug
	 * @return De arrayList met alle users uit het model
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

}
