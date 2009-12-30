package dk.moerks.ratebeermobile;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.adapters.PlacesAdapter;
import dk.moerks.ratebeermobile.io.LocationBroker;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.PlacesInfo;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class Places extends ListActivity {
	private static final String LOGTAG = "Places";
	private List<PlacesInfo> results = null;
	private ProgressDialog locationDialog = null;
    private Thread placesThread = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.places);
        
        locationDialog = ProgressDialog.show(Places.this, getText(R.string.places_retrieving), getText(R.string.places_retrieving_text));

    	locationDialog.setOnDismissListener(new ProgressDialog.OnDismissListener(){
    		public void onDismiss(DialogInterface dialog){
    				refreshList(Places.this, results);
    		}
    	});

    	placesThread = new Thread(){
    		public void run(){
    			Looper.prepare();
    			//Get Location
    			String locationString = LocationBroker.requestLocation(getApplicationContext());
    			
    			String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/json/beerme.asp?mi=15&"+locationString);
    			Log.d(LOGTAG, responseString);
    			
    			if(responseString != null){
    				try {
    					results = RBJSONParser.parsePlaces(responseString);
    				} catch(JSONException e){
    					Log.e(LOGTAG, "Error Parsing Places JSON response");
    					Looper.prepare();
        				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_parse_error), Toast.LENGTH_LONG);
       					toast.show();
    					Looper.loop();
    				}
    			} else {
    				results = null;
    			}
    			
    			locationDialog.dismiss();
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

    private void refreshList(Activity context, List<PlacesInfo> results){
    	PlacesAdapter adapter = new PlacesAdapter(context, results);
    	setListAdapter(adapter);
    }

}
