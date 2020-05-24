package skislitsyn;

public class ATMMemento {
    private final ATMState state;

    public ATMMemento(ATMState state) {
	this.state = new ATMState(state);
    }

    public ATMState getState() {
	return state;
    }
}
