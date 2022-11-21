package parser;

import exceptions.InvalidSymbolException;
import exceptions.ParserException;
import exceptions.UnmatchedBracketsException;
import nodes.AndNode;
import nodes.IfOnlyNode;
import nodes.IfThenNode;
import nodes.Node;
import nodes.NotNode;
import nodes.OrNode;
import nodes.VariableNode;

public class StatementParser {

	private static boolean startsEndsWithBrackets(String s) {
		return s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')';
	}

	private static boolean bracketMismatch(String s) {
		boolean a = s.charAt(0) == '(';
		boolean b = s.charAt(s.length() - 1) == ')';
		return a != b;
	}

	public static Node parse(String input) throws ParserException {
		Node root = null;

		input = input.trim();

		if (bracketMismatch(input))
			throw new UnmatchedBracketsException();

		if (!startsEndsWithBrackets(input))
			return new VariableNode(input);

		var tokens = Tokenizer.getTokens(input);
		if (tokens.size() == 0)
			throw new ParserException("empty token");

		final var name = tokens.get(0);
		switch (name) {
		case "and":
			root = new AndNode();
			break;
		case "or":
			root = new OrNode();
			break;
		case "not":
			root = new NotNode(null);
			break;
		case "if":
			root = new IfThenNode(null, null);
			break;
		case "bif":
			root = new IfOnlyNode(null, null);
			break;
		default:
			throw new InvalidSymbolException();
		}

		for (int i = 1; i < tokens.size(); i++) {
			String token = tokens.get(i);
			Node sub;
			// A recursive statement
			if (startsEndsWithBrackets(token)) {
				sub = parse(token);
			}
			// A variable node
			else {
				sub = new VariableNode(token);
			}

			root.addNode(sub);
		}

		root.verify();

		return root;
	}

}
