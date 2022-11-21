package exceptions;

public class InvalidParameterException extends ParserException {
	private static final long serialVersionUID = 1L;

	public InvalidParameterException() {
		super("Invalid parameter");
	}

}
