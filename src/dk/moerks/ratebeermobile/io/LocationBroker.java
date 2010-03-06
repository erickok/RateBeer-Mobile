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

import dk.moerks.ratebeermobile.exceptions.LocationException;
import dk.moerks.ratebeermobile.handlers.PlacesLocationListener;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class LocationBroker {
	private static final String LOGTAG = "LocationBroker";
	
	public static String requestLocation(Context context) throws LocationException {
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
				try {
					Log.d(LOGTAG, "Falling Back to Network Provider...");
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 500.0f, locListener);
					latPoint = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
					longPoint = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
					locationManager.removeUpdates(locListener);
				} catch(NullPointerException npe){
					throw new LocationException(LOGTAG, "Unable to obtain location!", npe);
				}
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
