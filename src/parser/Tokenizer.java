package parser;

import java.util.ArrayList;
import java.util.Stack;

import exceptions.ParserException;
import exceptions.UnmatchedBracketsException;

public class Tokenizer {
	private static int getBalancedBracketIndex(String input, int startIdx, int endIdx)
			throws UnmatchedBracketsException {
		Stack<Character> count = new Stack<>();

		for (int i = startIdx; i < endIdx; i++) {
			char c = input.charAt(i);
			if (c == '(')
				count.add(c);
			else if (c == ')') {
				if (count.size() > 0) {
					count.pop();
					if (count.size() == 0)
						return i;
				} else
					break;
			}
		}

		throw new UnmatchedBracketsException();
	}

	static ArrayList<String> getTokens(String input) throws ParserException {
		ArrayList<String> out = new ArrayList<>();

		String tok = "";
		for (int i = 1; i < input.length() - 1; i++) {
			char c = input.charAt(i);

			if (c == '(') {
				int endIdx = getBalancedBracketIndex(input, i, input.length() - 1);
				out.add(input.substring(i, endIdx + 1));
				i = endIdx;
			} else if (c == ' ') {
				if (tok != "")
					out.add(tok);
				tok = "";
			} else {
				tok += c;
			}
		}

		if (tok != "")
			out.add(tok);

		return out;
	}
}
