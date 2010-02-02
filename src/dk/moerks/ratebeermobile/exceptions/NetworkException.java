package dk.moerks.ratebeermobile.exceptions;

import android.content.Context;

public class NetworkException extends RBException {
	private static final long serialVersionUID = -3490133595541874885L;

	public NetworkException(String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
	}
	
	public NetworkException(Context context, String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
		alertUser(context, message);
	}

}
