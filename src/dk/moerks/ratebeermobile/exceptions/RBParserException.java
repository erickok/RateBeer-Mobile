package dk.moerks.ratebeermobile.exceptions;

public class RBParserException extends RBException {
	private static final long serialVersionUID = -218628304793630775L;
	
	public RBParserException(String parentClass, String message, Exception exception){
		super(parentClass, message, exception);
	}
}
