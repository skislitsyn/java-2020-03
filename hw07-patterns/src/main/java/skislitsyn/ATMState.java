package skislitsyn;

import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;

public class ATMState {
    private final ATM[] atms;

    public ATMState(ATM[] atms) {
	this.atms = atms;
    }

    public ATMState(ATMState state) {
	this.atms = SerializationUtils.clone(state.getATMs());
    }

    public ATM[] getATMs() {
	return atms;
    }

    @Override
    public String toString() {
	return "ATMState{" + "atms=" + (atms == null ? null : Arrays.asList(atms)) + '}';
    }
}
