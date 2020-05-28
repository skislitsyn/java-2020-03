package skislitsyn;

import org.apache.commons.lang3.SerializationUtils;

public class ATMState {
    private final ATM atm;

    public ATMState(ATM atm) {
	this.atm = atm;
    }

    public ATMState(ATMState state) {
	this.atm = SerializationUtils.clone(state.getATM());
    }

    public ATM getATM() {
	return atm;
    }

    @Override
    public String toString() {
	return "ATMState{" + "atm=" + atm + '}';
    }
}
