package dk.moerks.ratebeermobile.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.Home;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class SetDrinkingStatusTask extends BetterRBTask<String, String> {
	
	private Home home;
	
	public SetDrinkingStatusTask(Home activity) {
		super(activity, "Updating drink status...");
		this.home = activity;
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {
		
		// Post the new 'now drinking' status
		Log.d("NowDrinkingStatusTask", "Setting drink status to '" + params[0] + "'");
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("MyStatus", params[0]));
		NetBroker.doRBPost(context, "http://www.ratebeer.com/userstatus-process.asp", parameters);
		return params[0];
		
	}

	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		// Set the updated drinking status text
		home.onDrinkingStatusUpdated(result);
	}

}
