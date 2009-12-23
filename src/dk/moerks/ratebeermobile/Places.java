package dk.moerks.ratebeermobile;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import dk.moerks.ratebeermobile.io.LocationBroker;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class Places extends ListActivity {
	private static final String LOGTAG = "Places";
	private List<PlacesInfo> results = null;
	private ProgressDialog locationDialog = null;
    private Thread placesThread = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.places);
        
        locationDialog = ProgressDialog.show(Places.this, getText(R.string.beermail_retrieving), getText(R.string.beermail_retrieving_text));

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
	
    private void refreshList(Activity context, List<PlacesInfo> results){
    	//MessageAdapter adapter = new MessageAdapter(context, results);
    	//setListAdapter(adapter);
    }

}
