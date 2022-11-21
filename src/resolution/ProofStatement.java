package resolution;

import nodes.Node;

public class ProofStatement {
	Node node;
	String proofExplanation;
	boolean isUsed;

	public ProofStatement(Node node, String proofExplanation) {
		this.node = node;
		this.proofExplanation = proofExplanation;
		this.isUsed = false;
	}

	@Override
	public String toString() {
		return String.format("%-22s\t%s", node, proofExplanation);
	}
}
