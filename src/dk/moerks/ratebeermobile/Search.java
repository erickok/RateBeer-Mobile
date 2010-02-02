package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.adapters.SearchAdapter;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.BCPParser;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class Search extends ListActivity {
	private static final String LOGTAG = "Search";
	private static final int SEARCH_DIALOG = 1;
	private static final int BARCODE_ACTIVITY = 101;
	
	private String product = null;
	
	public static List<SearchResult> results = null;

	public static Search ACTIVE_INSTANCE;
	
	ProgressDialog searchDialog = null;

	final Handler threadHandler = new Handler();

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
        	
        	setProgressBarIndeterminateVisibility(false);
        }
    };

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ACTIVE_INSTANCE = this;
    	
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
            	ACTIVE_INSTANCE.showDialog(SEARCH_DIALOG);
            	
        		searchDialog.setOnDismissListener(new ProgressDialog.OnDismissListener(){
        			public void onDismiss(DialogInterface dialog){
        				if(results != null){
            				Log.d(LOGTAG, "RESULT SIZE: " + results.size());
        					refreshList(Search.this, results);
        				} else {
        					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_network_error), Toast.LENGTH_LONG);
        					toast.show();
        				}
        			}
        		});

            	
            	Thread searchThread = new Thread(){
            		public void run(){
	            		EditText searchText = (EditText) findViewById(R.id.searchText);
		    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		    			parameters.add(new BasicNameValuePair("BeerName", searchText.getText().toString()));  
		
		    			String responseString = NetBroker.doRBPost(getApplicationContext(), "http://www.ratebeer.com/findbeer.asp", parameters);
		    			if(responseString != null){
		    				try {
		    					results = RBParser.parseSearch(responseString);
		    				} catch(RBParserException e){
    	    					Log.e(LOGTAG, "There was an error parsing search data");
    	        				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_parse_error), Toast.LENGTH_LONG);
    	       					toast.show();
		    				}
		    			}
		    			ACTIVE_INSTANCE.dismissDialog(SEARCH_DIALOG);
            		}
            	};
            	searchThread.start();
            }
        });
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id){
    	case SEARCH_DIALOG:
    		searchDialog = new ProgressDialog(Search.this);
    		searchDialog.setTitle(getText(R.string.search_searching));
    		searchDialog.setMessage(getText(R.string.search_searching_text));
    	
        	return searchDialog;
    	}
    	
    	return null;
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
                
    			setProgressBarIndeterminateVisibility(true);
            	Thread bcThread = new Thread(){
            		public void run(){
            			Looper.prepare();
            			
            			//Do the network IO and parsing
		    			if(contents != null && contents.length() > 0){
		    				String bcUrl = "http://en.barcodepedia.com/"+contents;
		    				Log.d(LOGTAG, "BARCODE URL: " + bcUrl);
		    				String responseString = NetBroker.doGet(getApplicationContext(), bcUrl);
		    				product = BCPParser.parseBarcodeLookup(responseString);
		    			}
            			
		    			threadHandler.post(processDone);
		    			Looper.loop();
            		}
            	};
            	bcThread.start();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(LOGTAG, "ACTIVTIY RESULT WAS CANCELLED");
            }
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
}
