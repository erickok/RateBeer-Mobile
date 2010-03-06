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
package dk.moerks.ratebeermobile.activity;

import android.app.ListActivity;
import android.content.res.Configuration;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class RBActivity extends ListActivity {
	private static final String LOGTAG = "RBActivity";
	
	private static boolean inProgress = false;
	private static String userId = "";
	
	
	public final Handler threadHandler = new Handler();
    public final Runnable update = new Runnable() {
        public void run() {
        	update();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(LOGTAG, "CONFIGURATION CHANGED (Orientation)");
	}
	
	public void indeterminateStart(String progressMessage){
		setTitle(progressMessage);
		setProgressBarIndeterminateVisibility(true);
		setInProgress(true);
	}
	
	public void indeterminateStop(){
		setTitle("RateBeer Mobile");
		setProgressBarIndeterminateVisibility(false);
		setInProgress(false);
	}
	
	public void alertUser(String message){
		//Looper.prepare();
       	Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
   		toast.show();
   		//threadHandler.post(update);
		//Looper.loop();
	}
	
	protected void update(){
		indeterminateStop();
	}

	//Accessors
	public static void setInProgress(boolean inProgress) {
		RBActivity.inProgress = inProgress;
	}

	public static boolean isInProgress() {
		return inProgress;
	}

	public static void setUserId(String userId) {
		RBActivity.userId = userId;
	}

	public static String getUserId() {
		return userId;
	}
}
