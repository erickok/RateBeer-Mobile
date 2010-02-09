package dk.moerks.ratebeermobile.task;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.MailView;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class DeleteBeermailTask extends BetterRBTask<String, String> {

	// Used for the result callback
	private MailView mailView;
	
	public DeleteBeermailTask(MailView activity) {
		super(activity, "Deleting beermail...");
		this.mailView = activity;
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {

		// Delete a specific beermail message
		Log.d("DeleteBeermailTask", "Deleting beermail with ID " + params[0]);
		NetBroker.doRBGet(context, "http://ratebeer.com/DeleteMessage.asp?MessageID=" + params[0]);
		return params[0];
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		mailView.onBeermailDeleted(result);
	}
	
}
