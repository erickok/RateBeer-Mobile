package dk.moerks.ratebeermobile.io;

import dk.moerks.ratebeermobile.handlers.PlacesLocationListener;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class LocationBroker {
	private static final String LOGTAG = "LocationBroker";
	
	public static String requestLocation(Context context){
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager != null){
			PlacesLocationListener locListener = new PlacesLocationListener();
			String result;
			Double latPoint;
			Double longPoint;
			try {
				Log.d(LOGTAG, "Trying GPS Provider...");
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 500.0f, locListener);
				latPoint = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				longPoint = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
				locationManager.removeUpdates(locListener);
			} catch(Exception e){
				Log.d(LOGTAG, "Falling Back to Network Provider...");
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 500.0f, locListener);
				latPoint = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				longPoint = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
				locationManager.removeUpdates(locListener);
			}

			Log.d(LOGTAG, "LATITUDE : " + latPoint);
			Log.d(LOGTAG, "LONGITUDE: " + longPoint);
			result = "la=" + latPoint + "&lo=" + longPoint;
			
			Log.d(LOGTAG, "RESULT: " + result);
			
			return result;
		} else {
			Log.e(LOGTAG, "LocationManager is null");
		}
		
		return null;
	}
}
