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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import dk.moerks.ratebeermobile.BeerView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class SetDrinkingBeerIdTask extends BetterRBTask<String, String> {
	
	public SetDrinkingBeerIdTask(BeerView activity) {
		super(activity, R.string.task_updatedrinkstatus);
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {
		
		// Post the new 'now drinking' status using the beer name
		//TODO: This should be fixed so that we use the beer id instead, but apparently just visiting this url is not enough: http://www.ratebeer.com/userstatus-process.asp?BeerID=85934
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("MyStatus", params[0]));
		NetBroker.doRBPost(context, "http://www.ratebeer.com/userstatus-process.asp", parameters);
		return params[0];
		
	}

	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		// Set the updated drinking status text
		((BeerView)activity).onDrinkingStatusUpdated(result);
	}

}
