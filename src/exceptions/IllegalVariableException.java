package exceptions;

public class IllegalVariableException extends ParserException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableException() {
		super("Illegal variable");
	}

}
