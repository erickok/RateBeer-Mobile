package dk.moerks.ratebeermobile.task;

import android.content.Context;
import dk.moerks.ratebeermobile.BeerView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.BeerInfo;

public class RetrieveBeerInfoTask extends BetterRBTask<String, BeerInfo> {

	public RetrieveBeerInfoTask(BetterRBActivity activity) {
		super(activity, R.string.task_loadbeerinfo);
	}

	@Override
	protected BeerInfo doCheckedInBackground(Context context, String... params) throws Exception {

		// Get details for a specific beer by id
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/json/bff.asp?k=tTmwRTWT-W7tpBhtL&bd="+params[0]);
		return RBJSONParser.parseBeerInfo(responseString);
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, BeerInfo result) {
		((BeerView)activity).onInfoRetrieved(result);
	}
}
