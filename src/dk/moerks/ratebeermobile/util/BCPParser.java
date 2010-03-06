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
package dk.moerks.ratebeermobile.util;

import android.util.Log;

public class BCPParser {
	private static final String LOGTAG = "BCPParser";
	
	public static String parseBarcodeLookup(String responseString){
		String result = null;
		
		if(!responseString.contains("We have no data for this item")){
			Log.d(LOGTAG, "Barcode Found");
			//We have found a hit. Start parsing it
			
			int productBegin = responseString.indexOf("<h1>")+4;
			int productEnd = responseString.indexOf("</h1>", productBegin);
			result = responseString.substring(productBegin, productEnd);
		} else {
			Log.d(LOGTAG, "Barcode NOT Found");
		}
		
		return result;
	}
}
