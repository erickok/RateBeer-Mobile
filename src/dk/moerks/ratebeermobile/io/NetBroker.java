package dk.moerks.ratebeermobile.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import dk.moerks.ratebeermobile.Settings;

public class NetBroker {
	private static final String LOGTAG = "NetBroker";
	
	public static String doGet(Context context, String url) {
		DefaultHttpClient httpclient = init();
		if(url.contains("ratebeer.com")){
			if(!signin(context, httpclient)){
				Log.e(LOGTAG, "doGet - Ratebeer Login Failed");
				return null;
			}
		}
		
		Log.d(LOGTAG, "URL: " + url);
		HttpGet httpget = new HttpGet(url);  

		try {  
			// Execute HTTP Post Request  
			HttpResponse response = httpclient.execute(httpget);  

			String result = responseString(response);
			
			response.getEntity().consumeContent();
			
			return result; 
		} catch (ClientProtocolException e) {
			Log.e(LOGTAG, "doGet - ClientProtocolException");
		} catch (IOException e) {
			Log.e(LOGTAG, "doGet - IOException");
		} catch (Exception e){
			Log.e(LOGTAG, "doGet - Exception");
		}
		
		return null;
	}
	
	public static String doPost(Context context, String url, List<NameValuePair> parameters){
		DefaultHttpClient httpclient = init();
		
		if(url.contains("ratebeer.com")){
			if(!signin(context, httpclient)){
				Log.e(LOGTAG, "doPost - Ratebeer Login Failed");
				return null;
			}
		}
		
		HttpPost httppost = new HttpPost(url);  

		try {  
			// Add your data  
			httppost.setEntity(new UrlEncodedFormEntity(parameters));  
			
			// Execute HTTP Post Request  
			HttpResponse response = httpclient.execute(httppost);
			
			String result = responseString(response);
			
			response.getEntity().consumeContent();
			
			if(response.getStatusLine().getStatusCode() != 200){
				return null;
			}
			
			return result;
		} catch (ClientProtocolException e) {
			Log.e(LOGTAG, "doPost - ClientProtocolException");
		} catch (IOException e) {
			Log.e(LOGTAG, "doPost - IOException");
		}
		
		httpclient.getConnectionManager().shutdown();
		return null;
	}
	
	private static boolean signin(Context context, DefaultHttpClient httpclient){
		HttpPost httppost = new HttpPost("http://www.ratebeer.com/signin/");  
		Log.d(LOGTAG, "Before Try");
		try {  
			SharedPreferences settings = context.getSharedPreferences(Settings.PREFERENCETAG, 0);
			String username = settings.getString("rb_username", "");
			String password = settings.getString("rb_password", "");
			
			if(username != null && password != null && username.length()>0 && password.length() > 0){
				Log.d(LOGTAG, "Building Login Request");
				// Add your data  
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
				parameters.add(new BasicNameValuePair("SaveInfo", "on"));  
				parameters.add(new BasicNameValuePair("username", username));  
				parameters.add(new BasicNameValuePair("pwd", password));  
				httppost.setEntity(new UrlEncodedFormEntity(parameters));  
	
				//Check to see if we are already logged in
				List<Cookie> beforeCookies = httpclient.getCookieStore().getCookies();
				for (Iterator<Cookie> iterator = beforeCookies.iterator(); iterator.hasNext();) {
					Cookie cookie = iterator.next();
					if(cookie.getName().equalsIgnoreCase("SessionCode")){
						return true;
					}
				}
	
				Log.d(LOGTAG, "Executing Login Request");
				// Execute HTTP Post Request  
				HttpResponse result = httpclient.execute(httppost);
				int statusCode = result.getStatusLine().getStatusCode();
				
				Log.d(LOGTAG, "Validating we got the right status from server: " + statusCode);
				if(statusCode == 200){
					List<Cookie> cookies = httpclient.getCookieStore().getCookies();
					
					Log.d(LOGTAG, "Start Validating Cookies. List length: " + cookies.size());
					for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext();) {
						Cookie cookie = iterator.next();
						
						Log.d(LOGTAG, "Matching Cookie: " + cookie.getName());
						if(cookie.getName().equalsIgnoreCase("SessionCode")){
							result.getEntity().consumeContent();
							return true;
						}
					}
				} else {
					Log.e(LOGTAG, "There was an error executing request. Statuscode was: " + statusCode);
				}
			} else {
				Log.e(LOGTAG, "Username or Password not available in Shared Preferences");
			}
		} catch (ClientProtocolException e) { 
			Log.e(LOGTAG, "signin - ClientProtocolException");
		} catch (IOException e) {
			Log.e(LOGTAG, "signin - IOException");
		} catch (Exception e){
			Log.e(LOGTAG, "signin - Exception");
		}
		
		Log.d(LOGTAG, "FALSE - Signin failed");
		return false;
	}
	
	private static String responseString(HttpResponse response){
		try {
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();  
			response.getEntity().writeTo(ostream);
			//return ostream.toString("ISO8859_1");
			return ostream.toString("windows-1252"); 
		} catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static DefaultHttpClient init(){
		HttpParams params = new BasicHttpParams();
		params.setParameter("CookiePolicy", CookiePolicy.BROWSER_COMPATIBILITY);
		
		SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        registry.register(new Scheme("https",SSLSocketFactory.getSocketFactory(), 443));

		return new DefaultHttpClient(new ThreadSafeClientConnManager(params, registry), params);
	}
}
