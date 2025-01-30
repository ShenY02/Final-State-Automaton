package builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import components.Automaton;
import components.Node;

public class FSABuilder {
	private final Scanner scanner;
	private static FSABuilder instance = null;

	private FSABuilder(Scanner scanner) {
		this.scanner = scanner;
	}

	public static FSABuilder getBuilder(Scanner scanner) {
		if (instance == null) {
			instance = new FSABuilder(scanner);
		}

		return instance;
	}

	private void checkExit(String line) {
		if (line.strip().equalsIgnoreCase("exit")) {
			scanner.close();
			System.exit(0);
		}
	}

	private static void transitionFormat() {
		System.out.println("""
                                   Please, enter the transitions in the form of "symbol,next_state_number;".
                                   Note: whitespace characters are valid alphabetical symbols!
                                   Leave empty if there are no transitions:""");
	}

	private Character[] setAlphabet() {
		System.out.println("\nStep 1: Define the alphabet \u03A3");
		
	retry:
		while (true) {
			System.out.println("Please, enter the symbols of the alphabet, separated by commas:");
			System.out.println("Note: whitespace characters are considered as valid symbols!");
			System.out.print("\t\u03A3 = {\n\t\t");
			String line = scanner.nextLine();
			System.out.println("\t}");

			if (line.isEmpty()) continue;
			checkExit(line);

			String[] symbols = line.split(",");
			if (symbols.length == 0) continue;

			Character[] alphabet = new Character[symbols.length];

			for (int i = 0; i < symbols.length; i++) {
				if (symbols[i].length() != 1) continue retry;

				alphabet[i] = symbols[i].charAt(0);
			}

			return alphabet;
		}
	}

	private Node[] setStates() {
		System.out.println("\nStep 2: Define the set of states S");
		
		int number;
		while (true) {
			System.out.println("Please, enter the number of desired states: ");
			System.out.print("\t|S| = ");
			String line = scanner.nextLine().strip();

			if (line.isEmpty()) continue;

			if (line.equalsIgnoreCase("exit")) {
				scanner.close();
				System.exit(0);
			}

			try {
				number = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				continue;
			}

			if (number < 1) continue;

			break;
		}

		Node[] nodes = new Node[number];
		
		int i = 0;
		while (i < number) {
			System.out.println("\nState \u2116 " + (i + 1) + ":");
			System.out.print("Is it final? (y/n): ");
			String line = scanner.nextLine().strip();

			checkExit(line);

			if (line.isEmpty() | !line.toLowerCase().matches("[ny]")) continue;

			nodes[i] = new Node(line.equalsIgnoreCase("y"), new HashMap<>());
			i++;
		}

		return nodes;
	}

	private Node[] setTransitions(Node[] states, Character[] alphabet) {
		System.out.println("\nStep 3: Define the set-transitions function \u03B4");
		transitionFormat();

		Node[] linkedStates = new Node[states.length];
		int i = 0;
	retry:
		while (i < states.length) {
			System.out.println("\n\tState \u2116 " + (i + 1) + ":");
			System.out.print("\tTransitions: \u03B4(" + (i + 1) + ") = {\n\t\t");
			String line = scanner.nextLine();
			String[] links = line.split(";");
			System.out.println("\t}");

			if (line.isEmpty()) {
				Map<Character, Node> transitions = new HashMap<>();
				linkedStates[i] = states[i];
				linkedStates[i].addTransitions(transitions);

				i++;
				continue;
			}

			if (links.length <= 0) {
				transitionFormat();
				continue;
			}

			Map<Character, Node> transitions = new HashMap<>();

			for (String s : links) {
				String[] link = s.split(",");
				
				if (link.length <= 0) {
					transitionFormat();
					continue retry;
				}

				String symbol = link[0];
				checkExit(symbol);

				if (symbol.length() != 1) {
					transitionFormat();
					continue retry;
				}

				boolean noSymbol = true;
				for (char c : alphabet) {
					noSymbol = (c != symbol.charAt(0));
					if (!noSymbol) break;
				}
				
				if (noSymbol) {
					transitionFormat();
					continue retry;
				}

				try {
					int nextState = Integer.parseInt(link[1].strip());
					transitions.put(symbol.charAt(0), states[nextState - 1]);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					transitionFormat();
					continue retry;
				}
			}

			linkedStates[i] = states[i];
			linkedStates[i].addTransitions(transitions);

			i++;
		}

		return linkedStates;
	}

	private Node chooseStart(Node[] states) {
		System.out.println("\nStep 4: Choose the start state s\u2080");
		do { 
			System.out.println("Please enter the number corresponding to the desired state (from 1 to " + states.length + "):");
			System.out.print("\ts\u2080 = ");

			String line = scanner.nextLine().strip();
			checkExit(line);

			try {
				int stateNumber = Integer.parseInt(line);
				if (stateNumber > 0 && stateNumber <= states.length)
					return states[stateNumber - 1];
			} catch (NumberFormatException e) {}
		} while (true);
	}

	public Automaton buildAutomaton() {
		Character[] alphabet = setAlphabet();
		Node[] states = setTransitions(setStates(), alphabet);
		Node start = chooseStart(states);

		return new Automaton(alphabet, states, start);
	}
}
