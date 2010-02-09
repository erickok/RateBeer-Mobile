package dk.moerks.ratebeermobile.task;

import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.MailAction;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class SendBeermailTask extends BetterRBTask<NameValuePair, Void> {

	private MailAction mailAction;
	private boolean isReply;

	public SendBeermailTask(MailAction activity, boolean isReply) {
		super(activity, "Sending beermail...");
		this.mailAction = activity;
		this.isReply = isReply;
	}

	@Override
	protected Void doCheckedInBackground(Context context, NameValuePair... params) throws Exception {
		
		// Post the new 'now drinking' status
		Log.d("SendBeermailTask", "Sending a beermail with " + params.toString());
		List<NameValuePair> parameters = Arrays.asList(params);
		if(isReply){
			NetBroker.doRBPost(context, "http://ratebeer.com/SaveMessage.asp", parameters);
    	} else {
			NetBroker.doRBPost(context, "http://ratebeer.com/savemessage/", parameters);
    	}
		return null;
		
	}

	@Override
	protected void afterTask(BetterRBActivity activity, Void result) {
		// Report that the mail was send
		mailAction.onBeermailSend();
	}

}
