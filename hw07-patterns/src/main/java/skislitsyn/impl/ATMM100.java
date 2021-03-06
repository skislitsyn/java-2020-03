package skislitsyn.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import skislitsyn.Banknote;
import skislitsyn.Nominal;
import skislitsyn.model.ATMModel;

public class ATMM100 extends ATMImpl {
    private static final long serialVersionUID = 1L;
    ATMModel model = ATMModel.M100;

    public ATMM100(Set<Nominal> nominalsAvailable, Map<String, String> config) {
	super(nominalsAvailable);
    }

    @Override
    public void doCashIn(List<Banknote> banknotes) throws IOException {
	System.out.println("Some specific CashIn implementation for " + model.getName());
	super.doCashIn(banknotes);
    };

    @Override
    public List<Banknote> doCashOut(long amountRequested) throws IOException {
	System.out.println("Some specific CashOut implementation for " + model.getName());
	return super.doCashOut(amountRequested);
    };

}
