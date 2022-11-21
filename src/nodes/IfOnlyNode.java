package nodes;

import exceptions.InvalidParameterException;
import exceptions.ParserException;

public class IfOnlyNode extends Node {
	Node left, right;

	public IfOnlyNode(Node left, Node right) {
		this.left = left;
		this.right = right;
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "(" + left + " ↔ " + right + ")";
	}

	@Override
	public void addNode(Node toAdd) throws ParserException {
		if (left == null)
			left = toAdd;
		else if (right == null)
			right = toAdd;
		else
			throw new InvalidParameterException();
	}
	
	@Override
	public void verify() throws ParserException {
		if (left == null || right == null) throw new InvalidParameterException();
		
		left.verify();
		right.verify();
	}
}
