package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The Automaton class, representing a finite state machine in the form of
 * an <code>ArrayList</code> of <code>Node</code>s, interlinked by user-defined
 * transitions.
 * 
 * @author Shener Yumer
 */
public class Automaton extends ArrayList<Node> {
	/**
	 * The alphabet with which the automaton operates.
	 */
	private final ArrayList<Character> ALPHABET = new ArrayList<>();

	/**
	 * The Automaton constructor, initializing the automaton by the given
	 * alphabet, with which the automaton will work, the list of states
	 * through which the automaton will transition, and the initial state
	 * of the automaton.
	 * 
	 * @param alphabet The alphabet, with which the automaton will operate.
	 * @param states The list of states of the automaton.
	 * @param start The initial state of the automaton.
	 */
	public Automaton(Character[] alphabet, Node[] states, Node start) {
		super();
		
		this.addAll(Arrays.asList(states));
		this.remove(start);
		this.addFirst(start);
		
		ALPHABET.addAll(Arrays.asList(alphabet));

		System.out.println("\n --- Automaton created successfully! --- ");
	}

	/**
	 * Method for the automaton to analyze a word.
	 * 
	 * @param w The word to be analyzed.
	 * @return <code>true</code> if the word is accepted, <code>false</code> otherwise.
	 */
	public boolean tryWord(String w) {
		Node current = this.getFirst();

		for (char c : w.toCharArray()) {
			if (!ALPHABET.contains(c)) return false;

			current = current.getNext(c);

			if (current == null) return false;
		}

		return current.isAccepted();
	}

	/**
	 * Method for activating the automaton. This method uses a passed scanner,
	 * reading each new word given, and then analyzing it using the
	 * automaton's system of states.
	 * 
	 * @param sc The scanner, used to read input words.
	 */
	public void activate(Scanner sc) {
		System.out.println("Please, enter a word to test, or enter \"exit\" to stop the automaton:");

		String line = sc.nextLine();
		while(!line.equalsIgnoreCase("exit")) {
			if (line.isEmpty()) {
				line = sc.nextLine();
				continue;
			}

			System.out.println("\"" + line + "\"" + (tryWord(line) ? "" : " not") + " accepted!\n");
			line = sc.nextLine();
		}

		System.out.println("\n --- Automaton deactivated successfully! --- ");
	}
}
