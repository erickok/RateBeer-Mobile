package dk.moerks.ratebeermobile.task;

import java.util.List;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Home;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Feed;

public class RefreshFriendFeedTask extends BetterRBTask<Void, RefreshFriendFeedTask.FriendFeedTaskResult> {

	// Used for the result callback
	private Home home;
	
	public RefreshFriendFeedTask(Home activity) {
		super(activity, "Refreshing friend feed...");
		this.home = activity;
	}

	@Override
	protected FriendFeedTaskResult doCheckedInBackground(Context context, Void... params) throws Exception {

		// Get an update on the friend feed
		Log.d("RefreshFriendFeedTask", "Loading the friend feed.");
		String responseString = NetBroker.doRBGet(context, "http://www.ratebeer.com/activity");
		FriendFeedTaskResult result = new FriendFeedTaskResult();
		result.drink = RBParser.parseDrink(responseString);
		result.feeds = RBParser.parseFeed(responseString);
		return result;
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, FriendFeedTaskResult result) {
		home.onFriendFeedRefreshed(result);
	}

	public class FriendFeedTaskResult {
		public String drink;
		public List<Feed> feeds;
	}
	
}
