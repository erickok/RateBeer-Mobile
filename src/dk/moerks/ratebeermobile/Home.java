package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.RBActivity;
import dk.moerks.ratebeermobile.adapters.FeedAdapter;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Feed;

public class Home extends RBActivity {
	private static final String LOGTAG = "Home";
	private String drink;
	private List<Feed> feeds;
	private boolean firstRun;
	private boolean buttonFocus;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Verify if this might be the first run of the application
		SharedPreferences settings = getApplicationContext().getSharedPreferences(Settings.PREFERENCETAG, 0);
		String username = settings.getString("rb_username", "");
		String password = settings.getString("rb_password", "");
		
		if(username != null && password != null && username.length()>0 && password.length() > 0){
			firstRun = false;
		} else {
			firstRun = true;
		}
                
        Button updateButton = (Button) findViewById(R.id.drinkingUpdateButton);
        Button searchButton = (Button) findViewById(R.id.searchMenuButton);
        Button beermailButton = (Button) findViewById(R.id.beermailMenuButton);
        Button placesButton = (Button) findViewById(R.id.placesMenuButton);
        
        EditText updateTextGen = (EditText) findViewById(R.id.drinkingText);
        updateTextGen.setHint(getText(R.string.drinking));
                
        searchButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
            	Intent searchIntent = new Intent(Home.this, Search.class);  
            	startActivity(searchIntent);  
            }
        });
        
        beermailButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
            	Intent beermailIntent = new Intent(Home.this, BeerMail.class);  
            	startActivity(beermailIntent);  
            }
        });

        placesButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
            	Intent placesIntent = new Intent(Home.this, Places.class);  
            	startActivity(placesIntent);  
            }
        });
        
        updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText updateText = (EditText) findViewById(R.id.drinkingText);
        		final String updateTextString = updateText.getText().toString();
        		drink = updateTextString;

        		if(buttonFocus){
        			indeterminateStart("Update Drinking Status...");
	            	Thread updateDrinkingThread = new Thread(){
	            		public void run(){
	            			Log.d(LOGTAG, "Update Drink Status");
			    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
			    			parameters.add(new BasicNameValuePair("MyStatus", updateTextString));  
			
			    			if(updateTextString.length() > 0){
			    				try {
			    					NetBroker.doRBPost(getApplicationContext(), "http://www.ratebeer.com/userstatus-process.asp", parameters);
			    					threadHandler.post(update);
			    				} catch(NetworkException e){
			    				} catch(LoginException e){
		    	    				indeterminateStop();
			    				}
			    			}
	            		}
	            	};
	            	updateDrinkingThread.start();
        		} else {
	                indeterminateStart("Refreshing Friend Feed...");
	    			Thread drinkThread = new Thread(){
	    	    		public void run(){
	            			try {
	            				String responseString = NetBroker.doRBGet(getApplicationContext(), "http://www.ratebeer.com/activity");

	            				drink = RBParser.parseDrink(responseString);
    	    					feeds = RBParser.parseFeed(responseString);
    	    					
    	    					threadHandler.post(update);
	    	    			} catch(RBParserException e){
	    	    			} catch(NetworkException e){
	    	    			} catch(LoginException e){
	    	    				alertUser(e.getAlertMessage());
	    	    			}
	    	    		}
	    	    	};
	    	    	drinkThread.start();
        		}
			}
		});
        
        updateButton.performClick();
    }

	@Override
	protected void onStart() {
		super.onStart();

		if(firstRun){
        	Intent settingsIntent = new Intent(Home.this, Settings.class);  
        	startActivity(settingsIntent);  
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onStart();
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, 0, 0, R.string.menu_item_settings);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 0:
			Intent settingsIntent = new Intent(this, Settings.class);
			startActivity(settingsIntent);
			return true;
		}
		
		return false;
	}
	
	protected void update(){
		//Update Drinking Status
		if(drink != null && drink.length() > 0){
			TextView updateStatusGen = (TextView) findViewById(R.id.drinkingStatus);
			updateStatusGen.setText("You are currently drinking " + drink);
		}

		//Update Activity List if there is any
		if(feeds != null && feeds.size() > 0){
			FeedAdapter adapter = new FeedAdapter(this, feeds);
			setListAdapter(adapter);
		}

		indeterminateStop();
	}
}