package nl.saxion.rn.projecttwitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserActivity extends Activity {
	private TwitterModel model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		TextView name = (TextView)findViewById(R.id.textViewName);
		TextView description = (TextView)findViewById(R.id.textViewDescription);
		TextView followersCount = (TextView)findViewById(R.id.textViewFollowers);
		TextView friendsCount = (TextView)findViewById(R.id.textViewFriends);
		TextView createdAt = (TextView)findViewById(R.id.textViewCreatedAt);
		
		
		Intent intent = getIntent();
		int position = intent.getIntExtra("Position", 0);
		
		TwitterApplication app = (TwitterApplication)getApplicationContext();
		model = app.getModel();
		
		User u = model.getUsers().get(position);
		
		name.setText("" + u.getName());
		description.setText("" + u.getDescription());
		createdAt.setText("" + u.getCreatedAt());
		followersCount.setText("Followers: " + u.getFollowersCount());
		friendsCount.setText("Friends: " + u.getFriendsCount());

		
	}
}
