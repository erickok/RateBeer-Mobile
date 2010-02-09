package dk.moerks.ratebeermobile;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.SearchAdapter;
import dk.moerks.ratebeermobile.task.BarcodeLookupTask;
import dk.moerks.ratebeermobile.task.SearchTask;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class Search extends BetterRBListActivity {
	private static final String LOGTAG = "Search";
	private static final int BARCODE_ACTIVITY = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        // If barcode scanner is available setup the search field
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
	            EditText searchText = (EditText) findViewById(R.id.searchText);
	            new SearchTask(Search.this).execute(searchText.getText().toString());
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
            	
            	// A barcode was scanned
                final String contents = intent.getStringExtra("SCAN_RESULT");
                final String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d(LOGTAG, "BC CONTENTS: " + contents);
                Log.d(LOGTAG, "BC FORMAT:   " + format);
                
                // Loop up the barcode to match some product
                new BarcodeLookupTask(this).execute(contents);
                
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(LOGTAG, "ACTIVTIY RESULT WAS CANCELLED");
            }
        }
    }
    
	public void onResultsRetrieved(List<SearchResult> results) {
    	if (results != null) {
    		setListAdapter(new SearchAdapter(this, results));
			if (results.size() <= 0) {
	    		// If no beers were found, show this in a text message
				((TextView)findViewById(android.R.id.empty)).setText(R.string.toast_search_empty);
	    	}
    	}
	}

	public void onBarcodeProductRetrieved(String result) {
		if(result != null && result.length() > 0){
			
			// Show the text of the found product in the search box
			Log.d(LOGTAG, "PRODUCT: " + result);
			EditText searchBox = (EditText) findViewById(R.id.searchText);
			searchBox.setText(result);

			// Start a search for the found product
			new SearchTask(this).execute(result);
			
		} else {
			Toast.makeText(getApplicationContext(), getText(R.string.toast_search_barcode), Toast.LENGTH_LONG).show();
		}
	}
	
}
