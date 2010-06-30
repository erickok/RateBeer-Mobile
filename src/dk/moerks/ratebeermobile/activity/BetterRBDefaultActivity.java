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

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.github.droidfu.activities.BetterDefaultActivity;

import dk.moerks.ratebeermobile.Settings;
import dk.moerks.ratebeermobile.exceptions.RBException;

public class BetterRBDefaultActivity extends BetterDefaultActivity implements BetterRBActivity {

	private static final String EXTRA_HAS_TASK = "has_running_task";
	private boolean hasTask;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_HAS_TASK, hasRunningTask());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hasRunningTask(savedInstanceState.getBoolean(EXTRA_HAS_TASK));
        this.setProgressBarIndeterminateVisibility(hasRunningTask());
    }

	public void reportError(RBException e) {

		// Show the error as Toast popup
		Toast.makeText(this, e.getAlertMessage(), Toast.LENGTH_LONG).show();
		
	}

	public Context getContext() {
		return this;
	}
	
	public void setTitle(String message) {
		super.setTitle(message);
	}

	public String getUserId() {
		return Settings.getUserId(getContext());
	}

	public void hasRunningTask(boolean hasTask) {
		this.hasTask = hasTask;
	}

	public boolean hasRunningTask() {
		return this.hasTask;
	}
	
}
