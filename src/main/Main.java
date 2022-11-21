package main;

import java.util.Scanner;

import exceptions.ParserException;
import nodes.Node;
import parser.StatementParser;
import resolution.ConjunctiveForm;
import resolution.KnowledgeBase;
import resolution.ResolutionSolver;

public class Main {
	public static void main(String[] args) throws ParserException {
		try (Scanner s = new Scanner(System.in)) {
			System.out.print("Enter number of premises (excluding conclusion): ");
			int statementCount = s.nextInt();
			s.nextLine();

			KnowledgeBase kb = new KnowledgeBase();

			for (int i = 0; i < statementCount; i++) {
				System.out.print("> ");
				Node root = StatementParser.parse(s.nextLine());
				root.verify();
				kb.addStatement(root);
			}
			System.out.print("conclusion> ");
			Node root = StatementParser.parse(s.nextLine());
			root.verify();
			
			
			kb.setConclusion(root);

			System.out.println("\nSolving:");
			
			var solver = new ResolutionSolver(kb);
			solver.solve();

			int i = 0;
			for (var statement : solver.statements) {
				System.out.printf("%d\t %s\n", i, statement);
				i++;
			}

			if (solver.wasSolved()) {
				System.out.println("\nproved!");
			} else {
				System.out.println("\ncould not prove!");
			}
		}
	}

	@SuppressWarnings("unused")
	private static void repl() {
		try (Scanner s = new Scanner(System.in)) {
			while (true) {
				try {
					System.out.print("> ");
					Node root = StatementParser.parse(s.nextLine());
					System.out.println("parsed: " + root);
					root = ConjunctiveForm.convert(root);
					System.out.println("cnf: " + root);
				} catch (ParserException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
