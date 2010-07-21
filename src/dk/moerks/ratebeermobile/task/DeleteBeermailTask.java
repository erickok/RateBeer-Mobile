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
import dk.moerks.ratebeermobile.MailView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class DeleteBeermailTask extends BetterRBTask<String, String> {

	public DeleteBeermailTask(MailView activity) {
		super(activity, R.string.task_deletebeermail);
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
		((MailView)activity).onBeermailDeleted(result);
	}
	
}
