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
	
	private Rate rate;
	
	public SaveRatingTask(Rate activity) {
		super(activity, "Saving new rating...");
		this.rate = activity;
	}

	@Override
	protected Void doCheckedInBackground(Context context, NameValuePair... params) throws Exception {
		
		// Post the new 'now drinking' status
		Log.d("NowDrinkingStatusTask", "Setting drink status to '" + params[0] + "'");
		List<NameValuePair> parameters = Arrays.asList(params);
		NetBroker.doRBPost(context, "http://www.ratebeer.com/saverating.asp", parameters);
		return null;
		
	}

	@Override
	protected void afterTask(BetterRBActivity activity, Void result) {
		// Show that the rating was succesfully updated
		rate.onRatingSaved();
	}

}
