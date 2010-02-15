package dk.moerks.ratebeermobile.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Search;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBJSONParser;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class SearchTask extends BetterRBTask<String, List<SearchResult>> {

	// Used for the result callback
	private Search search;
	
	public SearchTask(Search activity) {
		super(activity, "Searching...");
		this.search = activity;
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
		search.onResultsRetrieved(result);
	}
	
}
