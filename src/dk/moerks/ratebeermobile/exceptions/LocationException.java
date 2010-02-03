package dk.moerks.ratebeermobile.exceptions;

public class LocationException extends RBException {
	private static final long serialVersionUID = 1357339313612519107L;

	public LocationException(String parentClass, String message){
		super(parentClass, message, null);
	}
	public LocationException(String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
	}

}
