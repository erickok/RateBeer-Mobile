package dk.moerks.ratebeermobile.task;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;

public class RetrieveUserIdTask extends BetterRBTask<String, String> {
	private static final String LOGTAG = "RetrieveUserIdTask";
	
	public RetrieveUserIdTask(BetterRBActivity activity) {
		super(activity, "Retrieving UserId");
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {

		// Search RB using a query string
		Log.d(LOGTAG, "Getting UserId");
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/activity");
		return RBParser.parseUserId(responseString);
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		activity.setUserId(result);
	}

}
