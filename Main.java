import builder.FSABuilder;
import components.Automaton;
import java.util.Scanner;

/**
 * The main class of the project. Contains the main method,
 * which establishes the creation of the Finite State Automaton.
 * 
 * @author Shener Yumer
 */
public class Main {
	/**
	 * Private constructor for the Main class to prevent instantiation.
	 */
	private Main() {}

	/**
	 * Private helper method for welcoming the user to the
	 * FSA creation process.
	 */
	private static void welcome() {
		System.out.println(" --- Welcome to the Final-State Automaton builder! --- ");
		System.out.println("Please, follow the steps and create your desired automaton: A(\u03A3,S,s\u2080,\u03B4,F)");
	}

	/**
	 * The main method. The entry point of the program.
	 * 
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		welcome();
		Scanner sc = new Scanner(System.in);

		FSABuilder builder = FSABuilder.getBuilder(sc);
		Automaton automaton = builder.buildAutomaton();

		automaton.activate(sc);

		sc.close();
	}
}