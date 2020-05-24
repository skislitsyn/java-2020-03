package skislitsyn;

import java.util.Set;

import skislitsyn.impl.ATMM100;
import skislitsyn.impl.ATMM200;
import skislitsyn.impl.ATMM201;
import skislitsyn.model.ATMModel;

public class ATMFactory {
    private ATMFactory() {
    }

    public static ATM getATM(Set<Nominal> nominalsAvailable, ATMModel model) {
	switch (model) {
	case M100: {
	    return new ATMM100(nominalsAvailable);
	}
	case M200: {
	    return new ATMM200(nominalsAvailable);
	}
	case M201: {
	    return new ATMM201(nominalsAvailable);
	}
	default:
	    throw new IllegalArgumentException("Model not supported: " + model.getName());
	}
    }
}
