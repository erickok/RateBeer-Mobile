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

import java.util.List;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Places;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class RetrievePlacesTask extends BetterRBTask<String, List<PlacesInfo>> {

	public RetrievePlacesTask(Places activity) {
		super(activity, "Retrieving nearby places...");
	}

	@Override
	protected List<PlacesInfo> doCheckedInBackground(Context context, String... params) throws Exception {

		// Get an update on the friend feed
		Log.d("RetrievePlacesTask", "Retrieving nearby places.");
		String locationString = params[0];
		String responseString = NetBroker.doRBGet(context, "http://ratebeer.com/json/beerme.asp?mi=15&"+locationString);

		return RBJSONParser.parsePlaces(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, List<PlacesInfo> result) {
		((Places)activity).onPlacesRetrieved(result);
	}
	
}
