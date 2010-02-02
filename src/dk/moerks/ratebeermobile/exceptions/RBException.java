package dk.moerks.ratebeermobile.exceptions;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
	
	public void alertUser(Context context, String message){
		Looper.prepare();
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
		Looper.loop();
	}
	
	private void logException(){
		//Log Error Message
		Log.e(parentClass, message);
		
		//Log StackTrace if available
		if(originalException != null){
			Log.d(parentClass, originalException.getMessage());
		} else {
			Log.d(parentClass, "Originating Exception unknown!");
		}
	}
}
