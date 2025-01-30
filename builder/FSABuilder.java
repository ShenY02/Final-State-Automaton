package builder;

import components.Automaton;
import components.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Builder singleton class used to build a finite state machine (FSM) based
 * on the instructed sequential input given by the user.
 * 
 * @author Shener Yumer
 */
public class FSABuilder {
	/**
	 * The scanner used to parse the user's input.
	 */
	private final Scanner SCANNER;

	/**
	 * The finite state automaton instance.
	 */
	private static FSABuilder instance = null;

	/**
	 * Private constructor to prevent instantiation from outside.
	 * 
	 * @param scanner The scanner used to parse the user's input.
	 */
	private FSABuilder(Scanner scanner) {
		this.SCANNER = scanner;
	}

	/**
	 * The singleton instance getter, which instantiates the builder with the
	 * given scanner if it does not exist. Otherwise returns the already created instance.
	 * 
	 * @param scanner The scanner used to parse the user's input.
	 * @return The singleton instance of the builder.
	 */
	public static FSABuilder getBuilder(Scanner scanner) {
		if (instance == null) {
			instance = new FSABuilder(scanner);
		}

		return instance;
	}

	/**
	 * Helper method for determining whether the user wants to terminate
	 * the program or not.
	 * 
	 * @param line The user provided line.
	 */
	private void checkExit(String line) {
		if (line.strip().equalsIgnoreCase("exit")) {
			SCANNER.close();
			System.exit(0);
		}
	}

	/**
	 * Helper method for printing out the transitions formatting instructions.
	 */
	private static void transitionFormat() {
		System.out.println("""
                                   Please, enter the transitions in the form of "symbol,next_state_number;".
                                   Note: whitespace characters are valid alphabetical symbols!
                                   Leave empty if there are no transitions:""");
	}

	/**
	 * Method for initializing the list of characters, which are considered
	 * in valid in the automaton's working alphabet. Repeats the process until
	 * a valid alphabet is provided or the user terminates the session.
	 * 
	 * @return The list of valid characters.
	 */
	private Character[] setAlphabet() {
		System.out.println("\nStep 1: Define the alphabet \u03A3");
		
	retry:
		while (true) {
			System.out.println("Please, enter the symbols of the alphabet, separated by commas:");
			System.out.println("Note: whitespace characters are considered as valid symbols!");
			System.out.print("\t\u03A3 = {\n\t\t");
			String line = SCANNER.nextLine();
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

	/**
	 * The method for initializing the states of the automaton. The user enters the total number
	 * of states, then is prompted to determine whether the states are accepting or not.
	 * 
	 * @return The list of the automaton's states.
	 */
	private Node[] setStates() {
		System.out.println("\nStep 2: Define the set of states S");
		
		int number;
		// Until a valid number of states is given, repeats the process.
		while (true) {
			System.out.println("Please, enter the number of desired states: ");
			System.out.print("\t|S| = ");
			String line = SCANNER.nextLine().strip();

			if (line.isEmpty()) continue;

			if (line.equalsIgnoreCase("exit")) {
				SCANNER.close();
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
		
		// For each state, the user is asked if it is accepting or not.
		int i = 0;
		while (i < number) {
			System.out.println("\nState \u2116 " + (i + 1) + ":");
			System.out.print("Is it an accepting state? (y/n): ");
			String line = SCANNER.nextLine().strip();

			checkExit(line);

			if (line.isEmpty() | !line.toLowerCase().matches("[ny]")) continue;

			nodes[i] = new Node(line.equalsIgnoreCase("y"), new HashMap<>());
			i++;
		}

		return nodes;
	}

	/**
	 * Method for initializing the transitions links between states.
	 * For each state the user is prompted to enter a list of transitions
	 * separated by semicolons (<code>;</code>). Each transition consists of
	 * a character and the number, corresponding to the following state, separated
	 * by a comma (<code>,</code>). The process repeats until all information
	 * provided is acceptable.
	 * 
	 * @param states The list of the automaton's states.
	 * @param alphabet The automaton's alphabet.
	 * @return The list of the automaton's states with the transitions added.
	 */
	private Node[] setTransitions(Node[] states, Character[] alphabet) {
		System.out.println("\nStep 3: Define the set-transitions function \u03B4");
		transitionFormat();

		Node[] linkedStates = new Node[states.length];
		int i = 0;
	retry:
		while (i < states.length) {
			System.out.println("\n\tState \u2116 " + (i + 1) + ":");
			System.out.print("\tTransitions: \u03B4(" + (i + 1) + ") = {\n\t\t");

			String line = SCANNER.nextLine();
			String[] links = line.split(";");
			System.out.println("\t}");

			// If no transitions are provided, no error is provoked.
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

			// For each transition, splits the string into character and state.
			for (String s : links) {
				String[] mapping = s.split(",");
				
				if (mapping.length <= 0) {
					transitionFormat();
					continue retry;
				}

				String symbol = mapping[0];
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
					int nextState = Integer.parseInt(mapping[1].strip());
					transitions.put(symbol.charAt(0), states[nextState - 1]);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					transitionFormat();
					continue retry;
				}
			}

			// When all provided information is valid, adds the transitions to each state respectively.
			linkedStates[i] = states[i];
			linkedStates[i].addTransitions(transitions);

			i++;
		}

		return linkedStates;
	}

	/**
	 * Method for selection of the initial state of the automaton.
	 * The user is prompted to provide the number corresponding to the state,
	 * which is to be considered the initial state for the automaton.
	 * 
	 * @param states The list of the automaton's states.
	 * @return The selected initial state of the automaton.
	 */
	private Node chooseStart(Node[] states) {
		System.out.println("\nStep 4: Choose the start state s\u2080");
		do { 
			System.out.println("Please enter the number corresponding to the desired state (from 1 to " + states.length + "):");
			System.out.print("\ts\u2080 = ");

			String line = SCANNER.nextLine().strip();
			checkExit(line);

			try {
				int stateNumber = Integer.parseInt(line);
				if (stateNumber > 0 && stateNumber <= states.length)
					return states[stateNumber - 1];
			} catch (NumberFormatException e) {}
		} while (true);
	}

	/**
	 * The method for the general initialization and build of the automaton.
	 * 
	 * @return The built automaton.
	 */
	public Automaton buildAutomaton() {
		Character[] alphabet = setAlphabet();
		Node[] states = setTransitions(setStates(), alphabet);
		Node start = chooseStart(states);

		return new Automaton(alphabet, states, start);
	}
}
