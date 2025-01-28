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
		// TODO: Accept words
		System.out.println("Tralala");

		String line = sc.nextLine().strip();
		while(!line.equalsIgnoreCase("exit")) {
			if (line.isEmpty()) {
				line = sc.nextLine().strip();
				continue;
			}
		}
	}
}
