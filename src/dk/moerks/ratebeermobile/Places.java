package dk.moerks.ratebeermobile;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import dk.moerks.ratebeermobile.activity.RBActivity;
import dk.moerks.ratebeermobile.adapters.PlacesAdapter;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.LocationBroker;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class Places extends RBActivity {
	private static final String LOGTAG = "Places";
	private List<PlacesInfo> results = null;
    private Thread placesThread = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);

        indeterminateStart("Retrieving Places...");
    	placesThread = new Thread(){
    		public void run(){
				Looper.prepare();
    			try {
    				String locationString = LocationBroker.requestLocation(getApplicationContext());
    				String responseString = NetBroker.doRBGet(getApplicationContext(), "http://ratebeer.com/json/beerme.asp?mi=15&"+locationString);

        			results = RBJSONParser.parsePlaces(responseString);
    			} catch(RBParserException e){
    			} catch(NetworkException e){
    			} catch(LoginException e){
    				alertUser(e.getAlertMessage());
       			}

    			threadHandler.post(update);
    			Looper.loop();
    		}
    	};
    	placesThread.start();
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

    protected void update(){
    	refreshList(Places.this, results);
    	indeterminateStop();
    }
    
    private void refreshList(Activity context, List<PlacesInfo> results){
    	PlacesAdapter adapter = new PlacesAdapter(context, results);
    	setListAdapter(adapter);
    }
}
