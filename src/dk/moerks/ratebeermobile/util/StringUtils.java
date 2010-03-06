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
