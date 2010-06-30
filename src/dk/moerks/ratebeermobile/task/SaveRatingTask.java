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

import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Rate;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class SaveRatingTask extends BetterRBTask<NameValuePair, Void> {
	
	public SaveRatingTask(Rate activity) {
		super(activity, "Saving new rating...");
	}

	@Override
	protected Void doCheckedInBackground(Context context, NameValuePair... params) throws Exception {
		
		// Post the new 'now drinking' status
		Log.d("SaveRatingTask", "Saving rating for '" + params[0] + "'");
		List<NameValuePair> parameters = Arrays.asList(params);
		NetBroker.doRBPost(context, "http://www.ratebeer.com/saverating.asp", parameters);
		return null;
		
	}

	@Override
	protected void afterTask(BetterRBActivity activity, Void result) {
		// Show that the rating was succesfully updated
		((Rate)activity).onRatingSaved();
	}

}
