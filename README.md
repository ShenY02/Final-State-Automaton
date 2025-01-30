A simple Final State Machine implementation in Java, where based on an instructed user input a Final State Automaton is build and can be used to check whether provided words would be accepted or not.

The Automaton is formally defined as:

A(Σ, S, s₀, δ, F)

where:
1. Σ is the alphabet, with which the automaton will work
2. S is the set of states, which the machine can transition into
3. s₀ is the initial state of the machine
4. δ(Σ⨯S) → S is the transition function, taking a character from the user-provided word and the current state, resulting in a following new state of the machine
5. F is the set of the final states (accepting states), for which if the machine concludes analyzing in one of these states, means that the word provided by the user was accepted
