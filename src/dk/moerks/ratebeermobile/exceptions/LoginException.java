package dk.moerks.ratebeermobile.exceptions;


public class LoginException extends RBException {
	private static final long serialVersionUID = -5866174632002389927L;

	public LoginException(String parentClass, String message){
		super(parentClass, message, null);
	}
	public LoginException(String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
	}
}
