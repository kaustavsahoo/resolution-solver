package nodes;

import exceptions.IllegalVariableException;
import exceptions.ParserException;

public class VariableNode extends Node {
	String variable;

	public VariableNode(String variable) {
		this.variable = variable;
	}

	public String getVariable() {
		return variable;
	}

	@Override
	public String toString() {
		return variable;
	}

	@Override
	public void addNode(Node toAdd) {
	}

	@Override
	public void verify() throws ParserException {
		// Propositional variables can only be a single upper case letter with an
		// optional number
		if (!variable.matches("^[A-Z][0-9]*$")) {
			throw new IllegalVariableException();
		}
	}
}
