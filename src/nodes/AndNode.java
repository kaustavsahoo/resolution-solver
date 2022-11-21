package nodes;

import java.util.ArrayList;
import java.util.Arrays;

import exceptions.InvalidParameterException;
import exceptions.ParserException;

public class AndNode extends Node {
	ArrayList<Node> children;

	public ArrayList<Node> getChildren() {
		return children;
	}

	public AndNode(Node... nodes) {
		this.children = new ArrayList<>(Arrays.asList(nodes));
	}

	@Override
	public String toString() {
		if (children.size() == 0)
			return "";

		StringBuilder s = new StringBuilder();

		s.append("(");
		s.append(children.get(0).toString());

		for (int i = 1; i < children.size(); i++) {
			s.append(" ∧ ");
			s.append(children.get(i).toString());
		}
		s.append(")");

		return s.toString();
	}

	@Override
	public void addNode(Node toAdd) {
		children.add(toAdd);
	}

	@Override
	public void verify() throws ParserException {
		if (this.children.size() < 2) throw new InvalidParameterException();
		
		for (Node child : children) {
			child.verify();
		}
	}
}
