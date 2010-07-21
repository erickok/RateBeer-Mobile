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
import android.util.Log;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.Home;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;

public class RetrieveUserIdTask extends BetterRBTask<String, String> {
	private static final String LOGTAG = "RetrieveUserIdTask";
	
	public RetrieveUserIdTask(BetterRBActivity activity) {
		super(activity, R.string.task_retrieveuserid);
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {

		// Search RB using a query string
		Log.d(LOGTAG, "Getting UserId");
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/activity");
		return RBParser.parseUserId(responseString);
		
	}

	@Override
	protected void handleError(Context context, Exception error) {
		super.handleError(context, error);
		// Also allow the activity to disable buttons on a login failure
		((Home)context).onFailedUserIdRetrieval(this, error);
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		((Home)activity).onUserIdRetrieved(result);
	}

}
