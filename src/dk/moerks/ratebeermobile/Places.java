package dk.moerks.ratebeermobile;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.PlacesAdapter;
import dk.moerks.ratebeermobile.exceptions.LocationException;
import dk.moerks.ratebeermobile.io.LocationBroker;
import dk.moerks.ratebeermobile.task.RetrievePlacesTask;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class Places extends BetterRBListActivity {
	private static final String LOGTAG = "Places";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);

        // Retrieve a list of nearby places
        try {
        	// Note that the location needs to be requested in the UI thread
        	String location = LocationBroker.requestLocation(this);
            new RetrievePlacesTask(this).execute(location);
		} catch (LocationException e) {
			reportError(e);
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
    
}
