package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.RBActivity;
import dk.moerks.ratebeermobile.adapters.SearchAdapter;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.BCPParser;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class Search extends RBActivity {
	private static final String LOGTAG = "Search";
	private static final int BARCODE_ACTIVITY = 101;
	
	private String product = null;
	
	public static List<SearchResult> results = null;

	// Create runnable for posting
    final Runnable processDone = new Runnable() {
        public void run() {
			if(product != null && product.length() > 0){
				Log.d(LOGTAG, "PRODUCT: " + product);
				EditText searchBox = (EditText) findViewById(R.id.searchText);
				searchBox.setText(product);
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_search_barcode), Toast.LENGTH_LONG);
				toast.show();
			}
        	
        	indeterminateStop();
        }
    };

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        //If barcodescanner is available setup the searchfield
        try {
        	getPackageManager().getPackageInfo("com.google.zxing.client.android", PackageManager.GET_ACTIVITIES);

            EditText searchTextfield = (EditText) findViewById(R.id.searchText);
            searchTextfield.setHint(R.string.bcHint);
            searchTextfield.setOnLongClickListener(new View.OnLongClickListener() {
    			public boolean onLongClick(View v) {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(intent, BARCODE_ACTIVITY);

                    return true;
    			}
            });
        } catch(PackageManager.NameNotFoundException e){
        	Log.d(LOGTAG, "BarcodeScanner is not installed");
        }
        
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		indeterminateStart("Searching...");
            	Thread searchThread = new Thread(){
            		public void run(){
	            		EditText searchText = (EditText) findViewById(R.id.searchText);
		    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		    			parameters.add(new BasicNameValuePair("BeerName", searchText.getText().toString()));  
		
		    			try {
		    				String responseString = NetBroker.doRBGet(getApplicationContext(), "http://www.ratebeer.com/json/s.asp?b="+searchText.getText().toString()+"&u="+getUserId());
		    				results = RBJSONParser.parseSearch(responseString);
		    			} catch(RBParserException e){
		    			} catch(NetworkException e){
		    			} catch(LoginException e){
		    				alertUser(e.getAlertMessage());
		    			}
		    			
		    			threadHandler.post(update);
            		}
            	};
            	searchThread.start();
            }
        });
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	SearchResult item = (SearchResult) getListView().getItemAtPosition(position);
    	Log.d(LOGTAG, "ITEM NAME: " + item.getBeerName());
    	if(item.isRated()){
        	Intent ratingIntent = new Intent(Search.this, Rating.class);  
        	ratingIntent.putExtra("BEERNAME", item.getBeerName());
        	ratingIntent.putExtra("BEERID", item.getBeerId());
        	startActivity(ratingIntent);  
    	} else {
        	Intent rateIntent = new Intent(Search.this, Rate.class);  
        	rateIntent.putExtra("BEERNAME", item.getBeerName());
        	rateIntent.putExtra("BEERID", item.getBeerId());
        	startActivity(rateIntent);  
    	}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BARCODE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                final String contents = intent.getStringExtra("SCAN_RESULT");
                final String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d(LOGTAG, "BC CONTENTS: " + contents);
                Log.d(LOGTAG, "BC FORMAT:   " + format);
                
    			indeterminateStart("Identifying Barcode...");
            	Thread bcThread = new Thread(){
            		public void run(){
            			//Do the network IO and parsing
		    			if(contents != null && contents.length() > 0){
		    				String bcUrl = "http://en.barcodepedia.com/"+contents;
		    				Log.d(LOGTAG, "BARCODE URL: " + bcUrl);
		    				try {
		    					String responseString = NetBroker.doGet(getApplicationContext(), bcUrl);
		    					product = BCPParser.parseBarcodeLookup(responseString);
		    				} catch(NetworkException e){
		    				}
		    			}
            			
		    			threadHandler.post(processDone);
            		}
            	};
            	bcThread.start();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(LOGTAG, "ACTIVTIY RESULT WAS CANCELLED");
            }
        }
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	EditText searchText = (EditText) findViewById(R.id.searchText);
    	
    	if(searchText.getText().length() > 0){
    		Log.d(LOGTAG, "Resuming! (Searching)");
    		Log.d(LOGTAG, "Performing Click!");
            Button searchButton = (Button) findViewById(R.id.searchButton);
            searchButton.performClick();
    	} else {
    		Log.d(LOGTAG, "Resuming! (Search Field empty)");
    	}
    }
    
    private void refreshList(Activity context, List<SearchResult> results){
    	if(results != null && results.size() > 0){
	    	SearchAdapter adapter = new SearchAdapter(context, results);
	    	setListAdapter(adapter);
    	} else {
			Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_search_empty), Toast.LENGTH_LONG);
			toast.show();
    	}
    }
    
    protected void update(){
    	Log.d(LOGTAG, "RESULT SIZE: " + results.size());
    	refreshList(Search.this, results);
    	indeterminateStop();
    }
}
