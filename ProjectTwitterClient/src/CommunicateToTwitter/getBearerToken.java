package CommunicateToTwitter;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Base64;

public class getBearerToken {

	private static final String API_KEY = "cvTfgu6ARayg6Ly4oMPNe53cu";
	private static final String API_SECRET = "qlouhSjduvfb9EAqz9iiCUQfgnBG0fCVsxTJX5q9S5HLzBwEvh";
	
	public getBearerToken() {
	//		Plak	key	en	secret	aan	elkaar	en	zorg	dat	er	nooit	‘rare’	
	//		tekens	inzitten	
	String	authString	=	API_KEY	+	":"	+	API_SECRET;
	String	base64	=	Base64.encodeToString(	
	authString.getBytes(),	Base64.NO_WRAP);	
	//		Maak	een	POST-request	
	HttpPost	request	=		
	new	HttpPost("https://api.twitter.com/oauth2/token");	
	//		Voeg	key+secret	toe	
	request.setHeader("Authorization",	"Basic "	+	base64);	
	request.setHeader("Content-Type",	"application/x-www-form-urlencoded;charset=UTF-8");	
	//		Vul	body:	vertel	dat	je	inlogt	met	app	key	en	secret	
	try {
		request.setEntity(	
		new	StringEntity("grant_type=client_credentials"));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
