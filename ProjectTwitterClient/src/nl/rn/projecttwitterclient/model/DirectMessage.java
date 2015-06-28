package nl.rn.projecttwitterclient.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DirectMessage {
	private String text;
	private String screen_name;
	
	/**
	 * Constructor voor de direct message.
	 * Vormt het opgegeven JSONObject om naar een direct message
	 * @param directMessage Het om te vormen JSONObject
	 */
	public DirectMessage(JSONObject directMessage) {
		try {
			this.text = directMessage.getString("text");
			this.screen_name = directMessage.getString("sender_screen_name");
		} catch (JSONException e) {
			Log.d("Direct Message", "Direct messsage mislukt");
		}
	}
	
	/**
	 * De toString van een direct message om deze weer te geven in een lijst
	 */
	@Override
	public String toString() {
		return text + "\n\nVan: " + screen_name;
	}

}
