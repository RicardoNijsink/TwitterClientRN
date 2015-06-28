package nl.rn.projecttwitterclient.model;

import org.json.JSONObject;

public class UserMention extends Entity {

	/**
	 * De constructor van een UserMention.
	 * Het opgegeven JSONObject wordt omgevormd naar een UserMention, in de superclass.
	 * @param userMention Het om te vormen JSONObject
	 */
	public UserMention(JSONObject userMention) {
		super(userMention);
	}

}
