package components;

import java.util.Map;

/**
 * The Node element class. Establishes the functionality
 * for interconnection with other nodes based on transitions,
 * depending on a given input and the allowed alphabet.
 * 
 * @author Shener Yumer
 */
public class Node {
	/**
	 * Boolean flag to indicate whether the node is an accepting state.
	 */
	private final boolean FINAL_STATE;

	/**
	 * A character-node map, where each key is a character from the
	 * alphabet and each node is the following state of the automaton.
	 */
	private final Map<Character, Node> TRANSITIONS;

	/**
	 * The Node constructor. Creates a new node with the given flag for
	 * accepting state and the map of all transitions starting from that node.
	 * Initially the map is empty.
	 * 
	 * @param finalState	Flag, indicating whether the node is an accepting state.
	 * @param transitions	The set of all transitions starting from this node.
	 */
	public Node(boolean finalState, Map<Character, Node> transitions) {
		this.FINAL_STATE = finalState;
		this.TRANSITIONS = transitions;
	}

	/**
	 * Method for adding additional transitions from this node.
	 * 
	 * @param transitions The set of all new transitions starting from this node.
	 */
	public void addTransitions(Map<Character, Node> transitions) {
		this.TRANSITIONS.putAll(transitions);
	}

	/**
	 * Getter method for the accepting state flag.
	 * 
	 * @return <code>true</code> if the node is an accepting state, <code>false</code> otherwise.
	 */
	public boolean isAccepted() {
		return FINAL_STATE;
	}

	/**
	 * Getter method for the next state of the automaton based on the given
	 * character.
	 * 
	 * @param key The character mapped to the next state.
	 * @return The next state node, or <code>null</code> if no transition is found.
	 */
	public Node getNext(char key) {
		return TRANSITIONS.get(key);
	}	
}
