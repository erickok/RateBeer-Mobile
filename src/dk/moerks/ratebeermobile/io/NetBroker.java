/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import android.graphics.drawable.Drawable;
import android.util.Log;
import dk.moerks.ratebeermobile.Settings;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;

public class NetBroker {
	private static final String LOGTAG = "NetBroker";
	
	public static String doGet(Context context, String url) throws NetworkException {
		return doGet(context, null, url);
	}
	
	public static Drawable doGetImage(Context context, DefaultHttpClient httpclient, String url) throws NetworkException {
		
		if(httpclient == null){
			httpclient = init();
		}
		
		Log.d(LOGTAG, "URL: " + url);
		HttpGet httpget = new HttpGet(url);  

		try {  
			// Execute HTTP Post Request  
			HttpResponse response = httpclient.execute(httpget);  
			Drawable result = responseDrawable(response);
			response.getEntity().consumeContent();
			
			return result; 
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e){
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		}
		
		return null;
	}
	
	public static String doGet(Context context, DefaultHttpClient httpclient, String url) throws NetworkException {
		
		if(httpclient == null){
			httpclient = init();
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
		} catch (IOException e) {
		} catch (Exception e){
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		}
		
		return null;
	}

	public static String doRBGet(Context context, String url) throws NetworkException, LoginException {
		DefaultHttpClient httpclient = init();

		signin(context, httpclient);
		
		return doGet(context, httpclient, url);
	}

	public static String doPost(Context context, String url, List<NameValuePair> parameters) throws NetworkException {
		return doPost(context, null, url, parameters);
	}
	
	public static String doPost(Context context, DefaultHttpClient httpclient, String url, List<NameValuePair> parameters) throws NetworkException {
		if(httpclient == null){
			httpclient = init();
		}
		
		HttpPost httppost = new HttpPost(url);
		//httppost.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 7.0; Windows 2000)");
 
		for (Iterator<NameValuePair> iterator = parameters.iterator(); iterator.hasNext();) {
			NameValuePair nameValuePair = iterator.next();
			Log.d(LOGTAG, nameValuePair.getName() + " :: " + nameValuePair.getValue());
		}
		
		try {  
			// Add your data  
			httppost.setEntity(new UrlEncodedFormEntity(parameters));  
			
			// Execute HTTP Post Request
			Log.d(LOGTAG, "Executing Post");
			HttpResponse response = httpclient.execute(httppost);
			Log.d(LOGTAG, "Post Executed");
			Log.d(LOGTAG, "Post Response Code: " + response.getStatusLine().getStatusCode());
			String result = responseString(response);
			Log.d(LOGTAG, "Response String length: " + result.length());
			//Log.d(LOGTAG, result);
			response.getEntity().consumeContent();
			
			if(response.getStatusLine().getStatusCode() != 200){
				return null;
			}
			
			return result;
		} catch (ClientProtocolException e) {
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		} catch (IOException e) {
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		} catch (Exception e){
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	public static String doRBPost(Context context, String url, List<NameValuePair> parameters) throws NetworkException, LoginException {
		DefaultHttpClient httpclient = init();

		Log.d(LOGTAG, "RBPost - Signin");
		signin(context, httpclient);
		
		return doPost(context, httpclient, url, parameters);
	}
	
	private static void signin(Context context, DefaultHttpClient httpclient) throws NetworkException, LoginException {
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
						return;
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
							return;
						}
					}
					throw new LoginException(LOGTAG, "Login to ratebeer.com failed. Check your credentials");
				} else {
					throw new LoginException(LOGTAG, "Login to ratebeer.com failed. Check your credentials");
				}
			} else {
				throw new LoginException(LOGTAG, "Login to ratebeer.com failed. Check your credentials");
			}
		} catch (ClientProtocolException e) { 
		} catch (IOException e) {
			throw new NetworkException(context, LOGTAG, "Network Error - Do you have a network connection?", e);
		}
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
	
	private static Drawable responseDrawable(HttpResponse response){
		try {
			Drawable result = Drawable.createFromStream(response.getEntity().getContent(), "tmp.jpg");
			Log.d(LOGTAG, "HEIGHT: " + result.getMinimumHeight());
			return result;
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
