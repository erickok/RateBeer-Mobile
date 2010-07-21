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

import android.content.Context;
import android.util.Log;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.MailView;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Message;

public class RetrieveBeermailTask extends BetterRBTask<String, Message> {

	public RetrieveBeermailTask(MailView activity) {
		super(activity, R.string.task_retrievebeermail);
	}

	@Override
	protected Message doCheckedInBackground(Context context, String... params) throws Exception {

		// Get a specific beermail message
		Log.d("RetrieveBeermailTask", "Retrieving beermail with ID " + params[0]);
		String responseString = NetBroker.doRBGet(context, "http://ratebeer.com/showmessage/"+params[0]+"/");
		return RBParser.parseMessage(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, Message result) {
		((MailView)activity).onBeermailRetrieved(result);
	}
	
}
