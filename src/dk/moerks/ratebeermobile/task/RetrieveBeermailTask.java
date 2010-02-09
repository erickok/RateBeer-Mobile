package dk.moerks.ratebeermobile.task;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.MailView;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Message;

public class RetrieveBeermailTask extends BetterRBTask<String, Message> {

	// Used for the result callback
	private MailView mailView;
	
	public RetrieveBeermailTask(MailView activity) {
		super(activity, "Loading beermail...");
		this.mailView = activity;
	}

	@Override
	protected Message doCheckedInBackground(Context context, String... params) throws Exception {

		// Get a specific beermail message
		Log.d("RetrieveBeermailTask", "Retrieving beermail with ID " + params[0]);
		String responseString = NetBroker.doRBGet(context, "http://ratebeer.com/showmessage/"+params[0]+"/");
		return RBParser.parseMessage(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, Message result) {
		mailView.onBeermailRetrieved(result);
	}
	
}
