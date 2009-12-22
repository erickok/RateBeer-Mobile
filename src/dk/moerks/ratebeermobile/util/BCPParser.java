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
