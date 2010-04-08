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

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import dk.moerks.ratebeermobile.overlays.PinOverlayItem;
import dk.moerks.ratebeermobile.util.StringUtils;

public class PlaceView extends MapActivity {
	//private String placeId = null;
	private String placeName = null;
	private String placeType = null;
	private String placeAddress = null;
	private String placeCity = null;
	//private String placeStateId = null;
	//private String placeCountryId = null;
	//private String placePostalCode = null;
	private String placePhoneNumber = null;
	private String placeAvgRating = null;
	private String placePhoneAC = null;
	private String placeLatitude = null;
	private String placeLongitude = null;
	//private String placeDistance = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_view);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	//placeId = extras.getString("PLACEID");
        	placeName = extras.getString("PLACENAME");
        	placeType = extras.getString("PLACETYPE");
        	placeAddress = extras.getString("PLACEADDRESS");
        	placeCity = extras.getString("PLACECITY");
        	//placeStateId = extras.getString("PLACESTATEID");
        	//placeCountryId = extras.getString("PLACECOUNTRYID");
        	placePhoneNumber = extras.getString("PLACEPHONENUMBER");
        	placeAvgRating = extras.getString("PLACEAVGRATING");
        	placePhoneAC = extras.getString("PLACEPHONEAC");
        	placeLatitude = extras.getString("PLACELAT");
        	placeLongitude = extras.getString("PLACELNG");
        	//placeDistance = extras.getString("PLACEDISTANCE");
        } else {
        	//placeId = "Not Set!";
        	placeName = "Not Set!";
        	placeType = "Not Set!";
        	placeAddress = "Not Set!";
        	placeCity = "Not Set!";
        	//placeStateId = "Not Set!";
        	//placeCountryId = "Not Set!";
        	placePhoneNumber = "Not Set!";
        	placeAvgRating = "Not Set!";
        	placePhoneAC = "Not Set!";
        	placeLatitude = "Not Set!";
        	placeLongitude = "Not Set!";
        	//placeDistance = "Not Set!";
        }

        TextView placeNameText = (TextView) findViewById(R.id.place_view_name);
        placeNameText.setText(placeName);

        TextView placeTypeText = (TextView) findViewById(R.id.place_view_type);
        placeTypeText.setText(placeTypeToString(placeType));

        TextView placeRatingText = (TextView) findViewById(R.id.place_view_rating);
        if(!placeAvgRating.equalsIgnoreCase("null")){
        	placeRatingText.setText(getText(R.string.place_avgrating) + " " + StringUtils.roundNumberString(placeAvgRating));
        } else {
        	placeRatingText.setText(getText(R.string.place_avgrating) + " N/A - Not enough ratings!");
        }
        TextView placeAddressText = (TextView) findViewById(R.id.place_view_address);
        placeAddressText.setText(placeAddress);

        TextView placeCityText = (TextView) findViewById(R.id.place_view_city);
        placeCityText.setText(placeCity);

    	RelativeLayout placeAddress = (RelativeLayout) findViewById(R.id.place_address);
    	placeAddress.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startRouteIntent();
			}
		});

        TextView placePhoneText = (TextView) findViewById(R.id.place_view_phone);
    	placePhoneText.setText(getFormattedPhoneNumber());

    	RelativeLayout placePhone = (RelativeLayout) findViewById(R.id.place_phone);
    	placePhone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startCallIntent();
			}
		});

        MapView map = (MapView) findViewById(R.id.place_view_map);
        MapController mc = map.getController();

        //Add this place to the map
        List<Overlay> listOfOverlays = map.getOverlays();

        double lat = Double.parseDouble(placeLatitude);
        double lng = Double.parseDouble(placeLongitude);
        
        Drawable pin = this.getResources().getDrawable(R.drawable.redpushpin);
        Rect pinRect = new Rect(0, 0, 48, 48);
        pinRect.offset(0, -48);
        pin.setBounds(pinRect);
        PinOverlayItem pinOverlays = new PinOverlayItem(pin);
        GeoPoint point = new GeoPoint((int)(lat * 1E6),(int)(lng * 1E6));
        OverlayItem overlayitem = new OverlayItem(point, placeName, placeName);
        pinOverlays.addItem(overlayitem);
        listOfOverlays.add(pinOverlays); 

        mc.animateTo(point);
        mc.setZoom(15);
	}
	
	private String getFormattedPhoneNumber() {
        String number = placePhoneNumber;
        if(placePhoneAC != null && !placePhoneAC.equalsIgnoreCase("Not Set!") && !placePhoneAC.equalsIgnoreCase("null")){
        	number = "(" + placePhoneAC + ") " + placePhoneNumber;
        }
        return number;
	}
	
	/**
	 * Starts an intent to call this 'place' (assuming it indeed has a phone number)
	 */
	private void startCallIntent() {
		if (getFormattedPhoneNumber() != null && !getFormattedPhoneNumber().equals("")) {
			final Uri callUri = Uri.parse("tel:" + getFormattedPhoneNumber());
			startActivity(new Intent(Intent.ACTION_VIEW, callUri));
		}
	}
	
	/**
	 * Starts and intent to plan a route to this 'place'
	 */
	private void startRouteIntent() {
		if (placeLatitude != null && !placeLatitude.equals("")) {
			// We cannot actually start a route, but we can ask the Google Maps app to show this place's location
			final Uri geoUri = Uri.parse("geo:" + placeLatitude + "," + placeLongitude + "?q=" + placeName);
			startActivity(new Intent(Intent.ACTION_VIEW, geoUri));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem call = menu.add(0, 0, 0, R.string.menu_item_call);
		call.setIcon(android.R.drawable.ic_menu_call);
		MenuItem route = menu.add(1, 1, 1, R.string.menu_item_route);
		route.setIcon(android.R.drawable.ic_menu_mapmode);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 0:
			startCallIntent();
			return true;
		case 1:
			startRouteIntent();
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	//Private Methods
	private String placeTypeToString(String placeType){
		if(placeType.equalsIgnoreCase("1")){
			return "Brewpub";
		} else if(placeType.equalsIgnoreCase("2")){
			return "Bar";
		} else if(placeType.equalsIgnoreCase("3")){
			return "Beer Store";
		} else if(placeType.equalsIgnoreCase("4")){
			return "Restaurant";
		} else if(placeType.equalsIgnoreCase("5")){
			return "Brewery";
		} else if(placeType.equalsIgnoreCase("6")){
			return "Homebrew Shop";
		} else {
			return "Unknown!";
		}
	}
}
