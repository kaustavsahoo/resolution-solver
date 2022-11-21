package exceptions;

public class UnmatchedBracketsException extends ParserException {
	private static final long serialVersionUID = 1L;

	public UnmatchedBracketsException() {
		super("Brackets not matched");
	}

}
