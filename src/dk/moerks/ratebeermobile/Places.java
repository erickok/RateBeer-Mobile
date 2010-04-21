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
package dk.moerks.ratebeermobile;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.PlacesAdapter;
import dk.moerks.ratebeermobile.task.RetrievePlacesTask;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class Places extends BetterRBListActivity implements LocationListener {
	private static final String LOGTAG = "Places";
	
	private static String provider = null;
	private static String locationString = null;
	private static LocationManager locationManager = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);

        // Retrieve a list of nearby places
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		if(locationManager != null){
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			provider = locationManager.getBestProvider(criteria, true);
			
			Log.d(LOGTAG, "Using "+ provider +" Provider...");
			locationManager.requestLocationUpdates(provider, 0, 500.0f, this);
		} else {
			Log.e(LOGTAG, "LocationManager is null");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(locationString != null){
			new RetrievePlacesTask(this).execute(locationString);
		} else {
			new RetrievePlacesTask(this).execute(convertLocation(locationManager.getLastKnownLocation(provider)));
		}
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	PlacesInfo item = (PlacesInfo) getListView().getItemAtPosition(position);
    	Log.d(LOGTAG, "ITEM NAME: " + item.getPlaceName());
       	Intent placeIntent = new Intent(Places.this, PlaceView.class);  
       	placeIntent.putExtra("PLACEID", item.getPlaceID());
       	placeIntent.putExtra("PLACENAME", item.getPlaceName());
       	placeIntent.putExtra("PLACETYPE", item.getPlaceType());
       	placeIntent.putExtra("PLACEADDRESS", item.getAddress());
       	placeIntent.putExtra("PLACECITY", item.getCity());
       	placeIntent.putExtra("PLACESTATEID", item.getStateID());
       	placeIntent.putExtra("PLACECOUNTRYID", item.getCountryID());
       	placeIntent.putExtra("PLACEPOSTALCODE", item.getPostalCode());
       	placeIntent.putExtra("PLACEPHONENUMBER", item.getPhoneNumber());
       	placeIntent.putExtra("PLACEAVGRATING", item.getAvgRating());
       	placeIntent.putExtra("PLACEPHONEAC", item.getPhoneAC());
       	placeIntent.putExtra("PLACELAT", item.getLatitude());
       	placeIntent.putExtra("PLACELNG", item.getLongitude());
       	placeIntent.putExtra("PLACEDISTANCE", item.getDistance());
        startActivity(placeIntent);  
    }

    public void onPlacesRetrieved(List<PlacesInfo> results){
    	if (results != null) {
    		setListAdapter(new PlacesAdapter(this, results));
			if (results.size() <= 0) {
	    		// If no places were found, show this in a text message
				((TextView)findViewById(android.R.id.empty)).setText(R.string.place_noplaces);
	    	}
    	}
    }

	public void onLocationChanged(Location location) {
		locationString = convertLocation(location);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
    
	private String convertLocation(Location location){
		Log.d(LOGTAG, "LATITUDE : " + location.getLatitude());
		Log.d(LOGTAG, "LONGITUDE: " + location.getLongitude());
		String result  = "la=" + location.getLatitude() + "&lo=" + location.getLongitude();
		
		Log.d(LOGTAG, "RESULT: " + result);
		
		return result;
	}
}
