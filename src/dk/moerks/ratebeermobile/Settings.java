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

import dk.moerks.ratebeermobile.receivers.BootReceiver;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends Activity {
	private static final String LOGTAG = "Settings";
	public static final String PREFERENCETAG = "RBMOBILE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences settings = getSharedPreferences(PREFERENCETAG, 0);
        
        EditText usernameGen = (EditText) findViewById(R.id.settings_value_username);
    	EditText passwordGen = (EditText) findViewById(R.id.settings_value_password);
    	CheckBox notificationGen = (CheckBox) findViewById(R.id.settings_checkbox_beermail_notification);
    	Spinner notificationIntervalGen = (Spinner) findViewById(R.id.settings_notification_interval);
    	usernameGen.setText(settings.getString("rb_username", ""));
    	passwordGen.setText(settings.getString("rb_password", ""));
        notificationGen.setChecked(settings.getBoolean("rb_notifications", true));
        notificationIntervalGen.setSelection(getNotificationIntervalIndex(settings.getString("rb_notification_interval", "")));
        Button saveButton = (Button) findViewById(R.id.settings_button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
            	Log.d(LOGTAG, "Saving Preferences");
            	EditText username = (EditText) findViewById(R.id.settings_value_username);
            	EditText password = (EditText) findViewById(R.id.settings_value_password);
            	CheckBox notification = (CheckBox) findViewById(R.id.settings_checkbox_beermail_notification);
            	Spinner notificationInterval = (Spinner) findViewById(R.id.settings_notification_interval);
            	
            	SharedPreferences settings = getSharedPreferences(PREFERENCETAG, 0);
            	SharedPreferences.Editor editor = settings.edit();
            	editor.putString("rb_username", username.getText().toString());
            	editor.putString("rb_password", password.getText().toString());
            	editor.putBoolean("rb_notifications", notification.isChecked());
            	String interval = (String) notificationInterval.getSelectedItem();
            	editor.putString("rb_notification_interval", interval);
            	editor.commit();
            	
            	if(notification.isChecked()){
            		BootReceiver.cancelAlarm();
            		BootReceiver.startAlarm(getApplicationContext());
            	}
            	
				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_settings_saved), Toast.LENGTH_SHORT);
				toast.show();
				
            	Intent homeIntent = new Intent(Settings.this, Home.class);  
            	startActivity(homeIntent);  
            }
        });
	}
	
	private int getNotificationIntervalIndex(String value) {
		if(value.equalsIgnoreCase("15 min")){
			return 0;
		}
		if(value.equalsIgnoreCase("30 min")){
			return 1;
		}
		if(value.equalsIgnoreCase("60 min")){
			return 2;
		}
		return -1;
	}
	@Override
	protected void onStart() {
		super.onStart();

		Log.d(LOGTAG, "Reading Preferences - onStart");
        SharedPreferences settings = getSharedPreferences(PREFERENCETAG, 0);        
        EditText usernameGen = (EditText) findViewById(R.id.settings_value_username);
    	EditText passwordGen = (EditText) findViewById(R.id.settings_value_password);
    	CheckBox notificationGen = (CheckBox) findViewById(R.id.settings_checkbox_beermail_notification);
    	usernameGen.setText(settings.getString("rb_username", ""));
    	passwordGen.setText(settings.getString("rb_password", ""));
    	notificationGen.setChecked(settings.getBoolean("rb_notifications", false));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onStart();
	}
}
