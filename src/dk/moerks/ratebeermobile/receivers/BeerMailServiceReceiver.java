package dk.moerks.ratebeermobile.receivers;

import dk.moerks.ratebeermobile.services.BeerMailService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BeerMailServiceReceiver extends BroadcastReceiver {
	private static final String LOGTAG = "BeerMailServiceReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(LOGTAG, "Running BeerMail Service");
		context.startService(new Intent(context, BeerMailService.class));
	}

}
