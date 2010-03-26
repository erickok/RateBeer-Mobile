package dk.moerks.ratebeermobile.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import dk.moerks.ratebeermobile.Settings;
import dk.moerks.ratebeermobile.exceptions.NetworkException;

public class TwitterPoster {

	private static final String LOGTAG = "TwitterPoster";

	private static final String TWITTER_UPDATE_URL = "http://api.twitter.com/1/statuses/update.json";
	private static final String SOURCE = "RateBeer Mobile";
	
	private String name;
	private String password;

	private TwitterPoster(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	/**
	 * Build a TwitterPoster based on the user preferences
	 * @param context The application context to get the user preferences from
	 * @return A reusable TwitterPoster object
	 */
	public static TwitterPoster buildFromPreferences(Context context) {
		
		// Retrieve the Twitter user credentials from the application preferences
		SharedPreferences settings = context.getSharedPreferences(Settings.PREFERENCETAG, 0);
		// And return the Twitter poster object instance
		return new TwitterPoster(
				settings.getString("rb_twitter_username", ""), 
				settings.getString("rb_twitter_password", ""));
		
	}
	
	/**
	 * Updates the Twitter status text of the user
	 * @param context Application context for error logging
	 * @param status The new status text
	 * @throws NetworkException When either a network error occurred or the Twitter status could not be set
	 */
	public void updateStatus(Context context, String status) throws NetworkException {

		// Send status update POST request
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		parameters.add(new BasicNameValuePair("status", status));
		HttpResponse result = postRequest(context, TWITTER_UPDATE_URL, parameters);
		
		// Check response result
		//String response = responseString(result);
		if (result.getStatusLine().getStatusCode() != 200) {
			throw new NetworkException(context, LOGTAG, "Update of Twitter status was unsuccessfull", null);
		}

	}

	private HttpResponse postRequest(Context context, String url, List<NameValuePair> parameters) throws NetworkException {

		// Set up our own HttpClient
		DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
		HttpPost post = new HttpPost(url);
		
		// Set RateBeer Mobile as twit source
		parameters.add(new BasicNameValuePair("source", SOURCE));
		
		// Basic authentication of our Twitter user
		client.getCredentialsProvider().setCredentials(
			new AuthScope("twitter.com", 80, AuthScope.ANY_REALM),
			new UsernamePasswordCredentials(this.name, this.password));
		
		try {
			
			// Send POST request
			post.setEntity(new UrlEncodedFormEntity(parameters));
			return client.execute(post);
			
		} catch (UnsupportedEncodingException e) {
			throw new NetworkException(context, LOGTAG, "Network Error - Cannot build Twitter request", e);
		} catch (ClientProtocolException e) {
			throw new NetworkException(context, LOGTAG, "Network Error - Cannot build Twitter request", e);
		} catch (IOException e) {
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		}  
		
	}

	/*private String responseString(HttpResponse response){
		try {
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();  
			response.getEntity().writeTo(ostream);
			//return ostream.toString("ISO8859_1");
			return ostream.toString("windows-1252"); 
		} catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}*/
	
}
