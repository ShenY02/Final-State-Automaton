import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	private static Scanner sc;

	private Main() {}

	private static void welcome() {
		System.out.println("Welcome to the Final-State Automaton builder!");
		System.out.println("Please, follow the steps and create your desired automaton: A(\u03A3,S,s\u2080,\u03B4,F)");
	}

	private static void checkExit(String line) {
		if (line.strip().equalsIgnoreCase("exit")) {
			sc.close();
			System.exit(0);
		}
	}

	private static Character[] setAlphabet() {
		System.out.println("\nStep 1: Define the alphabet \u03A3");
		
	retry:
		while (true) {
			System.out.println("Please, enter the symbols of the alphabet, separated by commas: ");
			System.out.print("\t\u03A3 = {\n\t\t");
			String line = sc.nextLine().strip();
			System.out.println("\t}");

			if (line.isEmpty()) continue;
			checkExit(line);

			String[] symbols = line.split(",");
			if (symbols.length == 0) continue;

			Character[] alphabet = new Character[symbols.length];

			for (int i = 0; i < symbols.length; i++) {
				symbols[i] = symbols[i].strip();
				if (symbols[i].length() != 1) continue retry;

				alphabet[i] = symbols[i].charAt(0);
			}

			return alphabet;
		}
	}

	private static Node[] setStates() {
		System.out.println("\nStep 2: Define the set of states S");
		
		int number;
		while (true) {
			System.out.println("Please, enter the number of desired states: ");
			System.out.print("\t|S| = ");
			String line = sc.nextLine().strip();

			if (line.isEmpty()) continue;

			if (line.equalsIgnoreCase("exit")) {
				sc.close();
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
			String line = sc.nextLine().strip();

			checkExit(line);

			if (line.isEmpty() | !line.toLowerCase().matches("[ny]")) continue;

			nodes[i] = new Node(line.equalsIgnoreCase("y"), new HashMap<>());
			i++;
		}

		return nodes;
	}

	private static Node[] setTransitions(Node[] states, Character[] alphabet) {
		System.out.println("\nStep 3: Define the set-transitions function \u03B4");
		System.out.println("Please, enter the transitions in the form of \"symbol, next_state_number;\".");
		System.out.println("Leave empty if there are no transitions:");

		Node[] linkedStates = new Node[states.length];
		int i = 0;
	retry:
		while (i < states.length) {
			System.out.println("\n\tState \u2116 " + (i + 1) + ":");
			System.out.print("\tTransitions: \u03B4(" + (i + 1) + ") = {\n\t\t");
			String line = sc.nextLine().strip();
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
				System.out.println("Please, enter the transitions in the form of \"symbol, next_state_number;\".");
				System.out.println("Leave empty if there are no transitions:");
				continue;
			}

			Map<Character, Node> transitions = new HashMap<>();

			for (String s : links) {
				String[] link = s.split(",");
				
				if (link.length <= 0) {
					System.out.println("Please, enter the transitions in the form of \"symbol, next_state_number;\".");
					System.out.println("Leave empty if there are no transitions:");
					continue retry;
				}

				String symbol = link[0].strip();
				checkExit(symbol);

				if (symbol.length() != 1) {
					System.out.println("Please, enter the transitions in the form of \"symbol, next_state_number;\".");
					System.out.println("Leave empty if there are no transitions:");
					continue retry;
				}

				boolean noSymbol = true;
				for (char c : alphabet) {
					noSymbol = (c != symbol.charAt(0));
					if (!noSymbol) break;
				}
				
				if (noSymbol) {
					System.out.println("Please, enter the transitions in the form of \"symbol, next_state_number;\".");
					System.out.println("Leave empty if there are no transitions:");
					continue retry;
				}

				try {
					int nextState = Integer.parseInt(link[1].strip());
					transitions.put(symbol.charAt(0), states[nextState - 1]);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					System.out.println("Please, enter the transitions in the form of \"symbol, next_state_number;\".");
					System.out.println("Leave empty if there are no transitions:");
					continue retry;
				}
			}

			linkedStates[i] = states[i];
			linkedStates[i].addTransitions(transitions);

			i++;
		}

		return linkedStates;
	}

	private static Node chooseStart(Node[] states) {
		System.out.println("\nStep 4: Choose the start state s\u2080");
		do { 
			System.out.println("Please enter the number corresponding to the desired state (from 1 to " + states.length + "):");
			System.out.print("\ts\u2080 = ");

			String line = sc.nextLine().strip();
			checkExit(line);

			try {
				int stateNumber = Integer.parseInt(line);
				if (stateNumber > 0 && stateNumber <= states.length)
					return states[stateNumber - 1];
			} catch (NumberFormatException e) {}
		} while (true);
	}

	public static void main(String[] args) {
		welcome();
		sc = new Scanner(System.in);
		
		Character[] alphabet = setAlphabet();
		Node[] states = setTransitions(setStates(), alphabet);
		Node start = chooseStart(states);

		Automaton automaton = new Automaton(alphabet, states, start);

		automaton.activate(sc);

		sc.close();
	}
}