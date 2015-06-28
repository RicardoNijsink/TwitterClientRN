package nl.rn.projecttwitterclient.model;

import org.json.JSONObject;

public class URL extends Entity{
	
	/**
	 * De constructor van een URL.
	 * Het opgegeven JSONObject wordt omgevormd naar een URL, in de superclass.
	 * @param url Het om te vormen JSONObject
	 */
	public URL(JSONObject url) {
		super(url);
	}
}
