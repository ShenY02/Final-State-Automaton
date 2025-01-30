package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Automaton extends ArrayList<Node> {
	private final ArrayList<Character> ALPHABET = new ArrayList<>();

	public Automaton(Character[] alphabet, Node[] states, Node start) {
		super();
		
		this.addAll(Arrays.asList(states));
		this.remove(start);
		this.addFirst(start);
		
		ALPHABET.addAll(Arrays.asList(alphabet));

		System.out.println("\n --- Automaton created successfully! --- ");
	}

	public boolean tryWord(String w) {
		Node current = this.getFirst();

		for (char c : w.toCharArray()) {
			if (!ALPHABET.contains(c)) return false;

			current = current.getNext(c);

			if (current == null) return false;
		}

		return current.isAccepted();
	}

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
