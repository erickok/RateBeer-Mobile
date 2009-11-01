package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.adapters.FeedAdapter;
import dk.moerks.ratebeermobile.adapters.SearchAdapter;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Feed;

public class Home extends ListActivity {
	private static final String LOGTAG = "Home";
	private String drink;
	private List<Feed> feeds;
	private boolean firstRun;
	final Handler threadHandler = new Handler();
    // Create runnable for posting
    final Runnable updateDrink = new Runnable() {
        public void run() {
        	updateDrinkView();
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Verify if this might be the first run of the application
		SharedPreferences settings = getApplicationContext().getSharedPreferences(Settings.PREFERENCETAG, 0);
		String username = settings.getString("rb_username", "");
		String password = settings.getString("rb_password", "");
		
		if(username != null && password != null && username.length()>0 && password.length() > 0){
			firstRun = false;
		} else {
			firstRun = true;
		}
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        setProgressBarIndeterminateVisibility(true);
        
        Button updateButton = (Button) findViewById(R.id.drinkingUpdateButton);
        Button searchButton = (Button) findViewById(R.id.searchMenuButton);
        Button beermailButton = (Button) findViewById(R.id.beermailMenuButton);
 
        EditText updateTextGen = (EditText) findViewById(R.id.drinkingText);
        
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
        
        updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText updateText = (EditText) findViewById(R.id.drinkingText);
        		final String updateTextString = updateText.getText().toString();
        		drink = updateTextString;
                setProgressBarIndeterminateVisibility(true);
            	Thread updateDrinkingThread = new Thread(){
            		public void run(){
            			Log.d(LOGTAG, "Update Drink Status");
            			Looper.prepare();
		    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		    			parameters.add(new BasicNameValuePair("MyStatus", updateTextString));  
		
		    			if(updateTextString.length() > 0){
		    				NetBroker.doPost(getApplicationContext(), "http://www.ratebeer.com/userstatus-process.asp", parameters);
		    				threadHandler.post(updateDrink);
		    			} else {
		   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_drink_empty), Toast.LENGTH_SHORT);
		   					toast.show();
		    			}
		    			Looper.loop();
            		}
            	};
            	updateDrinkingThread.start();
			}
		});
        
        
        updateTextGen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        	EditText updateTextFocus = (EditText) findViewById(R.id.drinkingText);
        	
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					updateTextFocus.setText("");
					updateTextFocus.setTextColor(Color.BLACK);
					Log.d(LOGTAG, "HAS FOCUS");
				} else {
					updateTextFocus.setText(getText(R.string.drinking));
					updateTextFocus.setTextColor(Color.LTGRAY);
					Log.d(LOGTAG, "NO FOCUS");
				}
			}
		});
    }

	@Override
	protected void onStart() {
		super.onStart();

		if(firstRun){
        	Intent settingsIntent = new Intent(Home.this, Settings.class);  
        	startActivity(settingsIntent);  
		} else {
			TextView updateStatusGen = (TextView) findViewById(R.id.drinkingStatus);
			updateStatusGen.setFocusable(true);
			updateStatusGen.setFocusableInTouchMode(true);
			updateStatusGen.requestFocus();
			
            setProgressBarIndeterminateVisibility(true);
			Thread drinkThread = new Thread(){
	    		public void run(){
	    			String responseString = NetBroker.doGet(getApplicationContext(), "http://www.ratebeer.com/activity");
	    			if(responseString != null){
	    				drink = RBParser.parseDrink(responseString);
	    				feeds = RBParser.parseFeed(responseString);
	    				threadHandler.post(updateDrink);
	    			} else {
	    				drink = null;
	    			}
	    		}
	    	};
	    	drinkThread.start();
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
	
	private void updateDrinkView(){
		if(drink != null){
			TextView updateStatusGen = (TextView) findViewById(R.id.drinkingStatus);

			updateStatusGen.setFocusable(true);
			updateStatusGen.setFocusableInTouchMode(true);
			updateStatusGen.requestFocus();
			
			//Update Drinking Status
			updateStatusGen.setText("You are currently drinking " + drink);

			//Update Activity List
			FeedAdapter adapter = new FeedAdapter(this, feeds);
		    setListAdapter(adapter);

			setProgressBarIndeterminateVisibility(false);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_network_error), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}