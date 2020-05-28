package skislitsyn.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skislitsyn.ATM;
import skislitsyn.ATMDepartment;
import skislitsyn.ATMOriginator;
import skislitsyn.ATMState;
import skislitsyn.Banknote;

public class ATMDepartmentImpl implements ATMDepartment {
    private final List<ATM> atms = new ArrayList<>();
    private final Map<ATM, ATMOriginator> atmOriginators = new HashMap<>();
    private final ATMGroup departmentATMs = new ATMGroup();

    public ATMDepartmentImpl() {
    }

    public ATMDepartmentImpl(List<ATM> atms) {
	for (ATM atm : atms) {
	    this.atms.add(atm);
	    departmentATMs.addATM(atm);
	    saveInitialATMState(atm);
	}
    }

    public void addATM(ATM atm) {
	this.atms.add(atm);
	departmentATMs.addATM(atm);
	saveInitialATMState(atm);
    }

    public List<ATM> getATMs() {
	return atms;
    }

    public void loadBanknotes(List<Banknote> banknotes) throws IOException {
	departmentATMs.doCashIn(banknotes);
    }

    public List<Banknote> getAmount(long amountRequested) throws IOException {
	return departmentATMs.doCashOut(amountRequested);
    }

    public long getRest() {
	return departmentATMs.getRest();
    }

    public void restoreInitialATMsState() {
	for (ATM atm : atms) {
	    ATMState initialATMState = atmOriginators.get(atm).restoreState();
	    ATM initialATM = initialATMState.getATM();
	    atms.set(atms.indexOf(atm), initialATM);
	    departmentATMs.replaceATM(atm, initialATM);
	}
    }

    private void saveInitialATMState(ATM atm) {
	ATMState atmState = new ATMState(atm);
	ATMOriginator atmOriginator = new ATMOriginator();
	atmOriginator.saveState(atmState);
	atmOriginators.put(atm, atmOriginator);
    }
}
