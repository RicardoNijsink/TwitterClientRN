package nl.saxion.rn.projecttwitterclient;

import nl.rn.projecttwitterclient.model.TwitterModel;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TimelineActivity extends Activity {

	private TwitterModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
	}
}