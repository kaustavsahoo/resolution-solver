package exceptions;

public class InvalidSymbolException extends ParserException {
	private static final long serialVersionUID = 1L;

	public InvalidSymbolException() {
		super("Invalid symbol");
	}

}
