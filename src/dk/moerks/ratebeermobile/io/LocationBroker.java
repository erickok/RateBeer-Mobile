package dk.moerks.ratebeermobile.io;

import dk.moerks.ratebeermobile.handlers.PlacesLocationListener;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class LocationBroker {
	private static final String LOGTAG = "LocationBroker";
	
	public static String requestLocation(Context context){
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 500.0f, new PlacesLocationListener());
		if(locationManager != null){
			Double latPoint = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
			Double longPoint = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			Log.d(LOGTAG, "LATITUDE : " + latPoint);
			Log.d(LOGTAG, "LONGITUDE: " + longPoint);
			String result = "la=" + latPoint + "&lo=" + longPoint;
			Log.d(LOGTAG, "RESULT: " + result);
			return result;
		} else {
			Log.e(LOGTAG, "LocationManager is null");
		}
		
		return null;
	}
}
