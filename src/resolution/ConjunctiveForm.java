package resolution;

import nodes.AndNode;
import nodes.IfOnlyNode;
import nodes.IfThenNode;
import nodes.Node;
import nodes.NotNode;
import nodes.OrNode;
import nodes.VariableNode;

public class ConjunctiveForm {
	public static Node convert(Node root) {
		var cnf = convertRecursive(root);
		return flattenRecursive(cnf);
	}

	static Node convertRecursive(Node root) {
		var rootClass = root.getClass();
		
		// Variable node
		if (rootClass == VariableNode.class)
			return root;
		
		// NOT node
		if (rootClass == NotNode.class) {
			var not = (NotNode) root;
			var child = not.getChild();
			// As is
			if (child.getClass() == VariableNode.class)
				return not;
			// Double negation
			if (child.getClass() == NotNode.class) {
				var grandchild = ((NotNode) child).getChild();
				return convertRecursive(grandchild);
			}
			// DeMorgan's Law (AND)
			if (child.getClass() == AndNode.class) {
				var and = (AndNode) child;
				var out = new OrNode();
				for (var andChild : and.getChildren()) {
					var negated = new NotNode(andChild);
					out.addNode(convertRecursive(negated));
				}
				return convertRecursive(out);
			}
			// DeMorgan's Law (OR)
			if (child.getClass() == OrNode.class) {
				var or = (OrNode) child;
				var out = new AndNode();
				for (var orChild : or.getChildren())
					out.addNode(new NotNode(orChild));
				return convertRecursive(out);
			}
			// Recursively call
			return convertRecursive(new NotNode(convertRecursive(child)));
		}

		// IfThenNode
		else if (rootClass == IfThenNode.class) {
			// Definition of implication
			var ifThen = (IfThenNode) root;
			var out = new OrNode();
			out.addNode(new NotNode(ifThen.getLeft()));
			out.addNode(ifThen.getRight());
			return convertRecursive(out);
		}

		// IfOnlyNode
		else if (rootClass == IfOnlyNode.class) {
			// Definition of biconditional
			var ifOnly = (IfOnlyNode) root;
			var out = new AndNode();
			out.addNode(new IfThenNode(ifOnly.getLeft(), ifOnly.getRight()));
			out.addNode(new IfThenNode(ifOnly.getRight(), ifOnly.getLeft()));
			return convertRecursive(out);
		}

		// ORNode
		else if (rootClass == OrNode.class) {
			// Converting each term separately
			var or = (OrNode) root;
			var out = new OrNode();
			for (var orChild : or.getChildren()) {
				out.addNode(convertRecursive(orChild));
			}
			return out;
		}

		// AndNode
		else if (rootClass == AndNode.class) {
			// Converting each term separately
			var and = (AndNode) root;
			var out = new AndNode();
			for (var andChild : and.getChildren()) {
				out.addNode(convertRecursive(andChild));
			}
			return out;
		}

		return null;
	}

	static Node flattenRecursive(Node root) {
		var rootClass = root.getClass();

		// Variable or Not node
		if (rootClass == VariableNode.class || rootClass == NotNode.class)
			return root;

		// Flatten AND
		if (rootClass == AndNode.class) {
			var and = (AndNode) root;
			var out = new AndNode();

			for (var child : and.getChildren()) {
				if (child.getClass() == AndNode.class) {
					var castedChild = (AndNode) flattenRecursive(child);
					for (var grandchild : castedChild.getChildren()) {
						out.addNode((grandchild));
					}
				} else {
					out.addNode(flattenRecursive(child));
				}
			}
			return out;
		}

		// Flatten OR
		if (rootClass == OrNode.class) {
			var or = (OrNode) root;
			var out = new OrNode();

			for (var child : or.getChildren()) {
				if (child.getClass() == OrNode.class) {
					var castedChild = (OrNode) flattenRecursive(child);
					for (var grandchild : castedChild.getChildren()) {
						out.addNode(grandchild);
					}
				} else {
					out.addNode(flattenRecursive(child));
				}
			}
			return out;
		}

		return root;
	}

}
