package nodes;

import exceptions.InvalidParameterException;
import exceptions.ParserException;

public class NotNode extends Node {
	Node child;

	public Node getChild() {
		return child;
	}

	public NotNode(Node node) {
		this.child = node;
	}

	@Override
	public String toString() {
		return "¬" + child;
	}

	@Override
	public void addNode(Node toAdd) throws ParserException {
		if (child == null)
			child = toAdd;
		else
			throw new InvalidParameterException();
	}
	
	@Override
	public void verify() throws ParserException {
		if (child == null) throw new InvalidParameterException();
		
		child.verify();
	}
}
