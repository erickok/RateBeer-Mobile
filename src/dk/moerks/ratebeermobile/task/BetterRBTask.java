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
package dk.moerks.ratebeermobile.task;

import android.content.Context;

import com.github.droidfu.concurrent.BetterAsyncTask;

import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.exceptions.RBException;

public abstract class BetterRBTask<ParameterT, ReturnT> extends BetterAsyncTask<ParameterT, Void, ReturnT> {
	
	private String progressMessage;
	
	public BetterRBTask(BetterRBActivity activity, String progressMessage) {
		super(activity.getContext());
		this.progressMessage = progressMessage;
	}

	private BetterRBActivity getActivity(Context context) {
		return (BetterRBActivity) context;
	}
	
	@Override
	protected final void before(Context context) {
		// Show a progress indicator text in the application title bar
		getActivity(context).setTitle(progressMessage);
		getActivity(context).hasRunningTask(true);
	}
	
	@Override
	protected final void handleError(Context context, Exception error) {
		// Always let the activity report any errors
		getActivity(context).reportError((RBException) error);
	}

	@Override
	protected final void after(Context context, ReturnT result) {
		// Show a progress indicator text in the application title bar
		getActivity(context).setTitle(context.getText(R.string.app_name).toString());
		getActivity(context).hasRunningTask(false);
		afterTask(getActivity(context), result);
	}
	
	/**
	 * Should implement the actual showing of the task results, such as showing the 
	 * returned/parsed list of search results. 
	 * @param activity The original RB activity that started the tast
	 * @param result The task results
	 */
	protected abstract void afterTask(BetterRBActivity activity, ReturnT result);
	
}
