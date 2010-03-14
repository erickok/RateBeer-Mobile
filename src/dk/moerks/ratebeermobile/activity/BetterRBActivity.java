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

import com.github.droidfu.activities.BetterActivity;

import dk.moerks.ratebeermobile.exceptions.RBException;

public interface BetterRBActivity extends BetterActivity {

	/**
	 * Should show an appropriate error message to the end user.
	 * @param e The exception that was thrown
	 */
	public abstract void reportError(RBException e);
	
	/**
	 * Should return the implementing activity's context
	 * @return The activity context, viz. itself
	 */
	public abstract Context getContext();

	/**
	 * Should set the activity's title
	 * @param message The message to show in the title bar
	 */
	public abstract void setTitle(String message);
	
	/**
	 * Gets the userid
	 * @return The RateBeer userid
	 */
	public abstract String getUserId();

	/**
	 * Sets whether the activity has a task running
	 * @param hasTask Whether the activity has a task running
	 */
	public abstract void hasRunningTask(boolean hasTask);

	/**
	 * Indicates whether the activity has a task running at the moment
	 * @return True is a task is running; false otherwise
	 */
	public abstract boolean hasRunningTask();
	
}
