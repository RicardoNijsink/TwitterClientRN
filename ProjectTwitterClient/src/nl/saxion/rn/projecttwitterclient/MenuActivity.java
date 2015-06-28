package nl.saxion.rn.projecttwitterclient;

import CommunicateToTwitter.TokenManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity {
	private TokenManager manager;

	/**
	 * Hier worden alle user interface-componenten en onClickListeners gedeclareerd
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		Button buttonZoeken = (Button) findViewById(R.id.buttonZoeken);
		Button buttonInloggen = (Button) findViewById(R.id.buttonInloggen);
		Button buttonTimeLine = (Button) findViewById(R.id.buttonTimeLine);
		TwitterApplication app = (TwitterApplication) getApplicationContext();

		manager = app.getManager();
		Log.d("Logged in", String.valueOf(manager.isLoggedIn()));

		buttonZoeken.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this,
						SearchActivity.class);
				startActivity(intent);
			}

		});

		buttonInloggen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!manager.isLoggedIn()) {
					Intent intent = new Intent(MenuActivity.this,
							SignInPage.class);
					startActivity(intent);
				}		else{
					Toast.makeText(getApplicationContext(), "U bent al ingelogd", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		buttonTimeLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(manager.isLoggedIn()){
					Intent intent = new Intent(MenuActivity.this, TimeLineActivity.class);
					startActivity(intent);
				}
				else{
					Toast.makeText(getApplicationContext(), "U moet ingelogd zijn om uw timeline te zien", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
