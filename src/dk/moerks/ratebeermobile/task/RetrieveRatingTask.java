package dk.moerks.ratebeermobile.task;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Rating;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.RatingData;

public class RetrieveRatingTask extends BetterRBTask<String, RatingData> {

	// Used for the result callback
	private Rating rating;
	
	public RetrieveRatingTask(Rating activity) {
		super(activity, "Loading beermail...");
		this.rating = activity;
	}

	@Override
	protected RatingData doCheckedInBackground(Context context, String... params) throws Exception {

		// Get a personal rating for a specific beer
		Log.d("RetrieveRatingTask", "Retrieving personal rating for beer with ID " + params[0]);
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/beer/rate/" + params[0] + "/");
		return RBParser.parseRating(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, RatingData result) {
		rating.onRatingRetrieved(result);
	}
	
}
