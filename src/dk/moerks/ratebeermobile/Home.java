/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.FeedAdapter;
import dk.moerks.ratebeermobile.task.RetrieveUserIdTask;
import dk.moerks.ratebeermobile.task.SetDrinkingStatusTask;
import dk.moerks.ratebeermobile.task.RefreshFriendFeedTask;
import dk.moerks.ratebeermobile.task.RefreshFriendFeedTask.FriendFeedTaskResult;

public class Home extends BetterRBListActivity {
	//private static final String LOGTAG = "Home";
	private boolean firstRun;
	private boolean hasNewDrinkText;
    
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
                
        final Button updateButton = (Button) findViewById(R.id.drinkingUpdateButton);
        Button searchButton = (Button) findViewById(R.id.searchMenuButton);
        Button beermailButton = (Button) findViewById(R.id.beermailMenuButton);
        Button placesButton = (Button) findViewById(R.id.placesMenuButton);
        
        final EditText updateTextGen = (EditText) findViewById(R.id.drinkingText);
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
        
        updateTextGen.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
				hasNewDrinkText = (s != null && s.length() > 0);
				if (hasNewDrinkText) {
					updateButton.setText(R.string.update);
				} else {
					updateButton.setText(R.string.refresh);
				}
			}
		});
        
        updateTextGen.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Set the 'now drinking' status when the 'enter' key is used
            		String updateTextString = updateTextGen.getText().toString();
        			new SetDrinkingStatusTask(Home.this).execute(updateTextString);
					return true;
				}
				return false;
			}
		});
        
        updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
        		if(hasNewDrinkText){
            		String updateTextString = updateTextGen.getText().toString();
        			new SetDrinkingStatusTask(Home.this).execute(updateTextString);
        		} else {
        			new RefreshFriendFeedTask(Home.this).execute();
        		}
			}
		});
        
        // Force an update of the friend feed on startup and retrieve userids
        if (!firstRun) {
        	new RefreshFriendFeedTask(this).execute();
        	
        	if(getUserId() == null || getUserId().length() <= 0){
        		new RetrieveUserIdTask(this).execute();
        	}
        }
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
	
	public void onFriendFeedRefreshed(FriendFeedTaskResult result){
		
		// Update Drinking Status
		if(result.drink != null && result.drink.length() > 0){
			onDrinkingStatusUpdated(result.drink);
		}

		// Update Activity List if there is any
		if(result.feeds != null){
			setListAdapter(new FeedAdapter(this, result.feeds));
			if (result.feeds.size() <= 0) {
				((TextView)findViewById(android.R.id.empty)).setText("");
			}
		}

	}

	public void onDrinkingStatusUpdated(String result) {

		// Update Drinking Status
		TextView updateStatusGen = (TextView) findViewById(R.id.drinkingStatus);
		updateStatusGen.setText("You are currently drinking " + result);

		// Updated, so clear the text box
        EditText updateTextGen = (EditText) findViewById(R.id.drinkingText);
        updateTextGen.setText("");
	}

	public void onUserIdRetrieved(String result){
		SharedPreferences settings = getApplicationContext().getSharedPreferences(Settings.PREFERENCETAG, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("rb_userid", result);
    	editor.commit();
	}
}