package nodes;

import exceptions.ParserException;

public abstract class Node {
	public abstract void addNode(Node toAdd) throws ParserException;
	public abstract void verify() throws ParserException;
}
