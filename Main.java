import builder.FSABuilder;
import components.Automaton;
import java.util.Scanner;

public class Main {
	private Main() {}

	private static void welcome() {
		System.out.println("Welcome to the Final-State Automaton builder!");
		System.out.println("Please, follow the steps and create your desired automaton: A(\u03A3,S,s\u2080,\u03B4,F)");
	}

	

	public static void main(String[] args) {
		welcome();
		Scanner sc = new Scanner(System.in);

		FSABuilder builder = FSABuilder.getBuilder(sc);
		Automaton automaton = builder.buildAutomaton();

		automaton.activate(sc);

		sc.close();
	}
}