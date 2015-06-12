package nl.rn.projecttwitterclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class Entity {
	private int beginPosition, endPosition;
	
	public Entity(JSONObject entity) {
		try {
			JSONArray indices = entity.getJSONArray("indices");
			beginPosition = indices.getInt(0);
			endPosition = indices.getInt(1);
		} catch (JSONException e) {
			Log.d("Entity parsen", "Mislukt");
		}
	}

	public int getBeginPosition() {
		return beginPosition;
	}

	public void setBeginPosition(int beginPosition) {
		this.beginPosition = beginPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	
	

}
