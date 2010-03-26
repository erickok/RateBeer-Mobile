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
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.TwitterPoster;

public class PostTwitterStatusTask extends BetterRBTask<String, String> {
	
	public PostTwitterStatusTask(BetterRBActivity activity) {
		super(activity, "Updating Twitter status...");
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {
		
		// Update the user's twitter status
		Log.d("PostTwitterStatusTask", "Updating the user's Twitter status to '" + params[0] + "'");
		TwitterPoster poster = TwitterPoster.buildFromPreferences(context);
		poster.updateStatus(context, params[0]);
		return params[0];
		
	}

	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		Toast.makeText((Context) activity, "Twitter status updated", Toast.LENGTH_SHORT).show();
	}

}
