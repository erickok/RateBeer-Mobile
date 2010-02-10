package dk.moerks.ratebeermobile.task;

import java.util.List;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.BeerMail;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.MessageHeader;

public class RetrieveBeermailsTask extends BetterRBTask<String, List<MessageHeader>> {

	// Used for the result callback
	private BeerMail beerMail;
	
	public RetrieveBeermailsTask(BeerMail activity) {
		super(activity, "Retrieving beermails...");
		this.beerMail = activity;
	}

	@Override
	protected List<MessageHeader> doCheckedInBackground(Context context, String... params) throws Exception {

		// Get a list of mails
		Log.d("RetrieveBeermailsTask", "Retrieving all beermails.");
		String responseString = NetBroker.doRBGet(context, "http://ratebeer.com/user/messages/");
		return RBParser.parseBeermail(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, List<MessageHeader> result) {
		beerMail.onBeermailsRetrieved(result);
	}
	
}
