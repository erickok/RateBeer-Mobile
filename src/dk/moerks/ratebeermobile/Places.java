package dk.moerks.ratebeermobile;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class Places extends ListActivity {
	private static final String LOGTAG = "Places";
	private List<PlacesInfo> results = null;
	private ProgressDialog locationDialog = null;
	private ProgressDialog placesDialog = null;
    private Thread placesThread = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.places);
        
        //Get Location
        locationDialog = ProgressDialog.show(Places.this, getText(R.string.beermail_retrieving), getText(R.string.beermail_retrieving_text));
    	placesDialog = ProgressDialog.show(Places.this, getText(R.string.beermail_retrieving), getText(R.string.beermail_retrieving_text));

    	locationDialog.setOnDismissListener(new ProgressDialog.OnDismissListener(){
    		public void onDismiss(DialogInterface dialog){
    	    	placesThread.start();
    		}
    	});
    	
    	placesDialog.setOnDismissListener(new ProgressDialog.OnDismissListener(){
    		public void onDismiss(DialogInterface dialog){
    			if(results != null){
    				refreshList(Places.this, results);
    			} else {
    				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_network_error), Toast.LENGTH_LONG);
   					toast.show();
    			}
    		}
    	});
    	
    	Thread locationThread = new Thread(){
    		public void run(){
       		}
    	};
    	locationThread.start();
    	
    	placesThread = new Thread(){
    		public void run(){
    			String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/user/messages/");
    			
    			if(responseString != null){
    				try {
    					results = RBParser.parsePlaces(responseString);
    				} catch(RBParserException e){
    					Log.e(LOGTAG, "Error Parsing Beermail response");
    					Looper.prepare();
        				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_parse_error), Toast.LENGTH_LONG);
       					toast.show();
    					Looper.loop();
    				}
    			} else {
    				results = null;
    			}
    			
    			placesDialog.dismiss();
       		}
    	};
    }
	
    private void refreshList(Activity context, List<PlacesInfo> results){
    	//MessageAdapter adapter = new MessageAdapter(context, results);
    	//setListAdapter(adapter);
    }

}
