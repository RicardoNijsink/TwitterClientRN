package nl.rn.projecttwitterclient.model;

import org.json.JSONObject;

public class HashTag extends Entity {
	
	/**
	 * De constructor van een HashTag.
	 * Het opgegeven JSONObject wordt omgevormd naar een HashTag, in de superclass.
	 * @param hashTag Het om te vormen JSONObject
	 */
	public HashTag(JSONObject hashTag) {
		super(hashTag);
	}
	
	

}
