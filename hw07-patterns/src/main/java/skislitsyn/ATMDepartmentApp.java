package skislitsyn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import skislitsyn.impl.ATMGroup;
import skislitsyn.impl.BanknoteImpl;
import skislitsyn.model.ATMModel;

public class ATMDepartmentApp {
    public static void main(String[] args) {
	// Prepare ATMs
	// For simplicity assume all ATMs work with the same nominals
	Nominal[] nominalsAvailable = { Nominal.N5000, Nominal.N1000, Nominal.N500 };

	// Factory method pattern
	ATM atm1 = ATMFactory.getATM(new HashSet<>(Arrays.asList(nominalsAvailable)), ATMModel.M100);
	ATM atm2 = ATMFactory.getATM(new HashSet<>(Arrays.asList(nominalsAvailable)), ATMModel.M200);
	ATM atm3 = ATMFactory.getATM(new HashSet<>(Arrays.asList(nominalsAvailable)), ATMModel.M201);

	// Memento pattern
	// Save initial ATMs state
	ATM[] atms = { atm1, atm2, atm3 };
	ATMState atmState = new ATMState(atms);
	ATMOriginator atmOriginator = new ATMOriginator();
	atmOriginator.saveState(atmState);

	// Composite pattern
	ATMGroup departmentATMs = new ATMGroup();
	departmentATMs.addATM(atm1);
	departmentATMs.addATM(atm2);
	departmentATMs.addATM(atm3);

	// Load banknotes to all department ATMs
	loadBanknotes(departmentATMs, nominalsAvailable, 100);
	// Get some amount from all department ATMs
	getAmount(departmentATMs, 5000);
	// Get rest from all department ATMs
	getRest(departmentATMs);

	// Restore initial ATMs state
	ATMState initialATMState = atmOriginator.restoreState();
	ATM[] initialATMs = initialATMState.getATMs();
	ATMGroup initialDepartmentATMs = new ATMGroup();
	for (ATM atm : initialATMs) {
	    initialDepartmentATMs.addATM(atm);
	}
	getRest(initialDepartmentATMs);
    }

    private static void loadBanknotes(ATMGroup departmentATMs, Nominal[] nominalsAvailable, int batchSize) {
	List<Banknote> banknotesForLoad = new ArrayList<>();
	for (Nominal nominal : nominalsAvailable) {
	    banknotesForLoad.addAll(getBanknotesForLoad(nominal, batchSize));
	}
	try {
	    departmentATMs.doCashIn(banknotesForLoad);
	    System.out.println("Amount loaded to department ATMs: " + departmentATMs.getRest());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static List<Banknote> getBanknotesForLoad(Nominal nominal, int quantity) {
	Banknote banknote = new BanknoteImpl(nominal);
	List<Banknote> banknotes = new ArrayList<>();
	for (int i = 0; i < quantity; i++) {
	    banknotes.add(banknote);
	}
	return banknotes;
    }

    private static void getAmount(ATMGroup departmentATMs, long amountRequested) {
	try {
	    System.out.println("Amount requested: " + amountRequested);
	    List<Banknote> cash = departmentATMs.doCashOut(amountRequested);
	    System.out.println("Cash received from department ATMs: " + cash);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static void getRest(ATMGroup departmentATMs) {
	System.out.println("The rest amount of department ATMs is: " + departmentATMs.getRest());
    }
}
