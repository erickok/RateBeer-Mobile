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
import dk.moerks.ratebeermobile.Home;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.BCPParser;

public class BarcodeLookupTask extends BetterRBTask<String, String> {

	public BarcodeLookupTask(Home home) {
		super(home, "Identifying barcode...");
	}

	@Override
	protected String doCheckedInBackground(Context context, String... params) throws Exception {

		// Look up a barcode to match it to some beer name (that can be used as query string)
		Log.d("BarcodeLookupTask", "Looking up barcode " + params[0]);
		String responseString = NetBroker.doGet(context, "http://en.barcodepedia.com/" + params[0]);
		return BCPParser.parseBarcodeLookup(responseString);
		
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, String result) {
		((Home)activity).onBarcodeProductRetrieved(result);
	}
	
}
