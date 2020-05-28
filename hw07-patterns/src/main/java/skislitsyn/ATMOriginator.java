package skislitsyn;

import java.util.ArrayDeque;
import java.util.Deque;

public class ATMOriginator {
    private final Deque<ATMMemento> stack = new ArrayDeque<>();

    public void saveState(ATMState state) {
	stack.push(new ATMMemento(state));
    }

    public ATMState restoreState() {
	return stack.pop().getState();
    }
}
