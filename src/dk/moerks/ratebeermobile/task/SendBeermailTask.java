/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile.task;

import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.MailAction;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class SendBeermailTask extends BetterRBTask<NameValuePair, Void> {
	private static final String LOGTAG = "SendBeermailTask";
	private boolean isReply;

	public SendBeermailTask(MailAction activity, boolean isReply) {
		super(activity, R.string.task_sendbeermail);
		this.isReply = isReply;
	}

	@Override
	protected Void doCheckedInBackground(Context context, NameValuePair... params) throws Exception {
		Log.d("SendBeermailTask", "Sending a beermail with " + params.toString());
		List<NameValuePair> parameters = Arrays.asList(params);
		
		Log.d(LOGTAG, "Is this a reply: " + isReply);
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
		Log.d(LOGTAG, "Reporting Mail Sent");
		((MailAction)activity).onBeermailSend();
	}

}
