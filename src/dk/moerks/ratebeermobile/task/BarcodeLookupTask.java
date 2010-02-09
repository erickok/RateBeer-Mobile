package dk.moerks.ratebeermobile.task;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Search;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.BCPParser;

public class BarcodeLookupTask extends BetterRBTask<String, String> {

	// Used for the result callback
	private Search search;
	
	public BarcodeLookupTask(Search activity) {
		super(activity, "Identifying barcode...");
		this.search = activity;
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {

		// Look up a barcode to match it to some beer name (that can be used as query string)
		Log.d("BarcodeLookupTask", "Looking up barcode " + params[0]);
		String responseString = NetBroker.doGet(context, "http://en.barcodepedia.com/" + params[0]);
		return BCPParser.parseBarcodeLookup(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		search.onBarcodeProductRetrieved(result);
	}
	
}
