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
package dk.moerks.ratebeermobile.exceptions;

import android.util.Log;

public class RBException extends Exception {
	private static final long serialVersionUID = -4771460057683617236L;

	private String message = null;
	private String parentClass = null;
	private Exception originalException = null;
	
	public RBException(){
		this.message = "An unexpected exception occured!";
		this.parentClass = "RBException";
		logException();
	}
	
	public RBException(String parentClass, String message, Exception exception){
		this.message = message;
		this.parentClass = parentClass;
		this.originalException = exception;
		logException();
	}
	
	private void logException(){
		//Log Error Message
		Log.e(parentClass, message);
		
		//Log StackTrace if available
		if(originalException != null && originalException.getMessage() != null){
			Log.d(parentClass, originalException.getMessage());
		} else {
			Log.d(parentClass, "Originating Exception unknown!");
		}
	}
	
	public String getAlertMessage(){
		return message;
	}
}
