package dk.moerks.ratebeermobile.util;

public class StringUtils {

	public static String roundNumberString(String number){
		if(number.contains(".")){
			int separator = number.indexOf(".");
			String beforeSeparator = number.substring(0,separator);
			String afterSeparator = number.substring(separator);
			if(afterSeparator.length() > 2){
				afterSeparator = afterSeparator.substring(0,2);
			}
			
			return beforeSeparator + "" + afterSeparator;
		} else {
			return number;
		}
	}

}
