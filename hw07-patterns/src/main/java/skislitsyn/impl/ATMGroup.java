package skislitsyn.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import skislitsyn.ATM;
import skislitsyn.Banknote;

public class ATMGroup implements ATM {
    private final List<ATM> atms = new ArrayList<>();

    public void addATM(ATM atm) {
	this.atms.add(atm);
    }

    public void replaceATM(ATM oldATM, ATM newATM) {
	this.atms.set(atms.indexOf(oldATM), newATM);
    }

    @Override
    public void doCashIn(List<Banknote> banknotes) throws IOException {
	for (ATM atm : atms) {
	    atm.doCashIn(banknotes);
	}
    }

    @Override
    public List<Banknote> doCashOut(long amountRequested) throws IOException {
	List<Banknote> result = new ArrayList<>();
	for (ATM atm : atms) {
	    result.addAll(atm.doCashOut(amountRequested));
	}
	return result;
    }

    @Override
    public long getRest() {
	long result = 0;
	for (ATM atm : atms) {
	    result += atm.getRest();
	}
	return result;
    }

}
