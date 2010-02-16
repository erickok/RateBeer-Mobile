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
