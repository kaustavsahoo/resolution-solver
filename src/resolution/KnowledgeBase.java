package resolution;

import java.util.ArrayList;

import nodes.Node;

public class KnowledgeBase {
	ArrayList<Node> statements;
	Node conclusion;
	
	public KnowledgeBase() {
		statements = new ArrayList<Node>();
	}

	public Node getConclusion() {
		return conclusion;
	}

	public void setConclusion(Node conclusion) {
		this.conclusion = conclusion;
	}

	public void addStatement(Node statement) {
		statements.add(statement);
	}

	public void display() {
		int i = 1;
		for (var statement : statements) {
			System.out.println(String.format("%d. %s", i, statement));
			i++;
		}
		System.out.println(String.format("[%d]. %s", i, conclusion));
	}
}
