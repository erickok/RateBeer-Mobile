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
import dk.moerks.ratebeermobile.BeerView;
import dk.moerks.ratebeermobile.Search;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.Review;

public class RetrieveBeerReviewsTask extends BetterRBTask<String, List<Review>>{

	public RetrieveBeerReviewsTask(BetterRBActivity activity) {
		super(activity, "Loading reviews...");
	}

	@Override
	protected List<Review> doCheckedInBackground(Context context, String... params) throws Exception {

		// Get a specific beermail message
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/json/gr.asp?k=tTmwRTWT-W7tpBhtL&bid="+params[0]);
		return RBJSONParser.parseReviews(responseString);
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, List<Review> result) {
		((BeerView)activity).onResultsRetrieved(result);
	}
}
