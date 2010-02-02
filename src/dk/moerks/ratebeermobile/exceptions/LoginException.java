package dk.moerks.ratebeermobile.exceptions;

import android.content.Context;

public class LoginException extends RBException {
	private static final long serialVersionUID = -5866174632002389927L;

	public LoginException(String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
	}
	
	public LoginException(Context context, String parentClass, String message){
		super(parentClass, message, null);
		alertUser(context, message);
	}
	
	public LoginException(Context context, String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
		alertUser(context, message);
	}
}
