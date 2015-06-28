package nl.saxion.rn.projecttwitterclient;

import java.util.concurrent.ExecutionException;

import CommunicateToTwitter.TokenManager;
import CommunicateToTwitter.TokenManager.GetRequestToken;
import CommunicateToTwitter.TokenManager.RetreiveAccessToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SignInPage extends Activity {
	TokenManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_page);
		
		WebView webview = (WebView)findViewById(R.id.webView1);
		TwitterApplication app = (TwitterApplication)getApplicationContext();
		
		manager = app.getManager();
		
		if(manager.isLoggedIn()){
			Intent intent = new Intent(SignInPage.this, TimeLineActivity.class);
			startActivity(intent);
		}
		
		GetRequestToken requestToken = new GetRequestToken();
		try {
			webview.loadUrl(requestToken.execute().get());
		} catch (InterruptedException e) {
			Log.d("Request token ophalen", "Ophalen mislukt");
		} catch (ExecutionException e) {
			Log.d("Request token ophalen", "Ophalen mislukt");
		}
		
		/**
		 * Controleert of de url in de webview begint met de callbackurl.
		 * Als dit het geval is dan wordt de verifier code opgehaald en daarmee wordt vervolgens de acces token opgehaald.
		 * Vervolgens wordt de TimeLineActivity gestart.
		 * Als dit niet het geval is, wordt de MenuActivity gestart.
		 */
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith(TokenManager.CALL_BACK_URL)){
					
					Uri verifierCode = Uri.parse(url);
					String oauth_verifier = verifierCode.getQueryParameter("oauth_verifier");
					
					if(oauth_verifier != null){
						Log.d("Verifier", oauth_verifier);
						manager.setLoggedIn(true);
						
						manager.setOauthVerifier(oauth_verifier);
						new RetreiveAccessToken().execute();
						
						Intent intent = new Intent(SignInPage.this, TimeLineActivity.class);
						startActivity(intent);
						finish();
					}
					else{
						Intent intent = new Intent(SignInPage.this, MenuActivity.class);
						startActivity(intent);
						finish();
					}
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in_page, menu);
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
