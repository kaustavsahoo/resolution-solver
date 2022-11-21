package resolution;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.ParserException;
import nodes.AndNode;
import nodes.EmptyNode;
import nodes.Node;
import nodes.NotNode;
import nodes.OrNode;
import nodes.VariableNode;

public class ResolutionSolver {
	public ArrayList<ProofStatement> statements;

	public ResolutionSolver(KnowledgeBase base) {
		statements = new ArrayList<ProofStatement>();
		generateStatements(base);
	}

	private void generateStatements(KnowledgeBase base) {
		for (int i = 0; i < base.statements.size(); i++) {
			var convertedStatement = ConjunctiveForm.convert(base.statements.get(i));
			var proof = String.format("premise as cnf", i);
			addWithoutDuplicates(new ProofStatement(convertedStatement, proof));
		}

		var negatedConclusion = ConjunctiveForm.convert(new NotNode(base.conclusion));
		addWithoutDuplicates(new ProofStatement(negatedConclusion, "negate cnf conclusion"));
	}

	public boolean wasSolved() {
		return countUnused() == 0 && countEmpty() > 0;
	}

	private int countUnused() {
		int unused = 0;
		for (var statement : statements) {
			// Don't count empty nodes
			if (statement.node.getClass() == EmptyNode.class)
				continue;
			if (!statement.isUsed)
				unused++;
		}
		return unused;
	}
	
	private int countEmpty() {
		int empty = 0;
		for (var statement : statements) {
			if (statement.node.getClass() == EmptyNode.class)
				empty++;
		}
		return empty;
	}

	private void addWithoutDuplicates(ProofStatement toAdd) {
		var str = toAdd.node.toString();
		for (ProofStatement statement : statements) {
			// Allow duplicates
			if (statement.node.getClass() == EmptyNode.class) {
				statement.isUsed = true;
				continue;
			}
			if (str.equals(statement.node.toString())) {
				return;
			}
		}
		statements.add(toAdd);
	}

	private void safeAdd(HashMap<String, ArrayList<Integer>> list, String key, Integer value) {
		list.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
	}

	private ProofStatement resolve(ResolutionPair pair) {
		Node finalNode = null;

		var normalStatement = statements.get(pair.normal());
		var negatedStatement = statements.get(pair.negated());

		normalStatement.isUsed = true;
		negatedStatement.isUsed = true;

		var normalNode = normalStatement.node;
		var negatedNode = negatedStatement.node;

		if (normalNode.getClass() == VariableNode.class && negatedNode.getClass() == NotNode.class) {
			finalNode = new EmptyNode();
		} else {
			finalNode = new OrNode();

			if (normalNode.getClass() == OrNode.class) {
				for (var child : ((OrNode) normalNode).getChildren()) {
					if (child.getClass() == VariableNode.class) {
						var varChild = (VariableNode) child;
						if (pair.variable().equals(varChild.getVariable().toString()))
							continue;
					}
					try {
						finalNode.addNode(child);
					} catch (ParserException e) {
						e.printStackTrace();
					}
				}
			}
			if (negatedNode.getClass() == OrNode.class) {
				for (var child : ((OrNode) negatedNode).getChildren()) {
					if (child.getClass() == NotNode.class) {
						var notNode = (NotNode) child;
						if (notNode.getChild().getClass() == VariableNode.class) {
							var varChild = (VariableNode) notNode.getChild();
							if (pair.variable().equals(varChild.getVariable().toString()))
								continue;
						}
					}
					try {
						finalNode.addNode(child);
					} catch (ParserException e) {
						e.printStackTrace();
					}
				}
			}

			var children = ((OrNode) finalNode).getChildren();
			if (children.size() == 1) {
				finalNode = children.get(0);
			}

		}

		var explanation = String.format("resolution of [%d, %d]", pair.normal(), pair.negated());
		return new ProofStatement(finalNode, explanation);
	}

	public void solve() {
		while (countUnused() > 0) {
			// Split all AndNodes into separate statements
			for (int i = 0; i < statements.size(); i++) {
				var statement = statements.get(i);
				if (statement.isUsed)
					continue;

				if (statement.node.getClass() == AndNode.class) {
					for (var child : ((AndNode) statement.node).getChildren()) {
						var proof = String.format("simplification of [%d]", i);
						addWithoutDuplicates(new ProofStatement(child, proof));
					}
					statement.isUsed = true;
				}
			}

			// Resolution
			HashMap<String, ArrayList<Integer>> normal = new HashMap<>(), negated = new HashMap<>();
			for (int i = 0; i < statements.size(); i++) {
				var statement = statements.get(i);

				var type = statement.node.getClass();
				// Add normal variables
				if (type == VariableNode.class) {
					var node = (VariableNode) statement.node;
					safeAdd(normal, node.getVariable(), i);
				}
				// Add negated variables
				else if (type == NotNode.class) {
					var node = (NotNode) statement.node;
					var childVariable = (VariableNode) node.getChild();
					safeAdd(negated, childVariable.getVariable(), i);
				} else if (type == OrNode.class) {
					for (var child : ((OrNode) statement.node).getChildren()) {
						// Add normal variables
						if (child.getClass() == VariableNode.class) {
							var node = (VariableNode) child;
							safeAdd(normal, node.getVariable(), i);
						}
						// Add negated variables
						else if (child.getClass() == NotNode.class) {
							var node = (NotNode) child;
							var childVariable = (VariableNode) node.getChild();
							safeAdd(negated, childVariable.getVariable(), i);
						}
					}
				}

			}

			ArrayList<ResolutionPair> pairs = new ArrayList<>();
			for (var entry : normal.entrySet()) {
				final var key = entry.getKey();
				if (negated.get(key) == null)
					continue;

				var normalArr = entry.getValue();
				var negatedArr = negated.get(key);

				int i = normalArr.size() - 1;
				int j = negatedArr.size() - 1;

				while (i >= 0 || j >= 0) {
					var x = normalArr.get(Math.max(0, i));
					var y = negatedArr.get(Math.max(0, j));
					if (!statements.get(x).isUsed || !statements.get(y).isUsed)
						pairs.add(new ResolutionPair(key, x, y));
					i--;
					j--;
				}
			}

			for (var pair : pairs) {
				var proof = resolve(pair);
				addWithoutDuplicates(proof);
			}

			if (pairs.size() == 0)
				return;
		}

	}

}
