package components;

import java.util.Map;

public class Node {
	private final boolean FINAL_STATE;
	private final Map<Character, Node> TRANSITIONS;

	public Node(boolean finalState, Map<Character, Node> transitions) {
		this.FINAL_STATE = finalState;
		this.TRANSITIONS = transitions;
	}

	public void addTransitions(Map<Character, Node> transitions) {
		this.TRANSITIONS.putAll(transitions);
	}

	public boolean isAccepted() {
		return FINAL_STATE;
	}

	public Node getNext(char key) {
		return TRANSITIONS.get(key);
	}	
}
