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
import dk.moerks.ratebeermobile.Search;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class SearchTask extends BetterRBTask<String, List<SearchResult>> {

	public SearchTask(Search activity) {
		super(activity, "Searching...");
	}

	@Override
	protected List<SearchResult> doCheckedInBackground(Context context, String... params) throws Exception {

		// Search RB using a query string
		Log.d("SearchTask", "Search for '" + params[0] + "' for user '"+params[1]+"'");
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/json/s.asp?b="+params[0]+"&u="+params[1]);
		return RBJSONParser.parseSearch(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, List<SearchResult> result) {
		((Search)activity).onResultsRetrieved(result);
	}
	
}
