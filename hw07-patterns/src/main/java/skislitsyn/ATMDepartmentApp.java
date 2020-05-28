package skislitsyn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import skislitsyn.impl.ATMDepartmentImpl;
import skislitsyn.impl.BanknoteImpl;
import skislitsyn.model.ATMModel;

public class ATMDepartmentApp {
    public static void main(String[] args) {
	// Prepare ATMs
	// For simplicity assume all ATMs work with the same nominals
	Nominal[] nominalsAvailable = { Nominal.N5000, Nominal.N1000, Nominal.N500 };

	ATM atm1 = ATMFactory.getATM(new HashSet<>(Arrays.asList(nominalsAvailable)), new HashMap<String, String>(),
		ATMModel.M100);
	ATM atm2 = ATMFactory.getATM(new HashSet<>(Arrays.asList(nominalsAvailable)), new HashMap<String, String>(),
		ATMModel.M200);
	ATM atm3 = ATMFactory.getATM(new HashSet<>(Arrays.asList(nominalsAvailable)), new HashMap<String, String>(),
		ATMModel.M201);

	ATMDepartment atmDepartment = new ATMDepartmentImpl();
	atmDepartment.addATM(atm1);
	atmDepartment.addATM(atm2);
	atmDepartment.addATM(atm3);

	// Load banknotes to all department ATMs
	loadBanknotes(atmDepartment, getBanknotesForLoad(nominalsAvailable, 100));
	// Get some amount from all department ATMs
	getAmount(atmDepartment, 5000);
	// Get rest from all department ATMs
	getRest(atmDepartment);

	// Restore initial ATMs state
	atmDepartment.restoreInitialATMsState();
	getRest(atmDepartment);
    }

    private static void loadBanknotes(ATMDepartment atmDepartment, List<Banknote> banknotes) {
	try {
	    atmDepartment.loadBanknotes(banknotes);
	    System.out.println("Amount loaded to department ATMs: " + atmDepartment.getRest());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static List<Banknote> getBanknotesForLoad(Nominal[] nominalsAvailable, int batchSize) {
	List<Banknote> banknotesForLoad = new ArrayList<>();
	for (Nominal nominal : nominalsAvailable) {
	    Banknote banknote = new BanknoteImpl(nominal);
	    List<Banknote> banknotes = new ArrayList<>();
	    for (int i = 0; i < batchSize; i++) {
		banknotes.add(banknote);
	    }
	    banknotesForLoad.addAll(banknotes);
	}
	return banknotesForLoad;
    }

    private static void getAmount(ATMDepartment atmDepartment, long amountRequested) {
	try {
	    System.out.println("Amount requested: " + amountRequested);
	    List<Banknote> cash = atmDepartment.getAmount(amountRequested);
	    System.out.println("Cash received from department ATMs: " + cash);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static void getRest(ATMDepartment atmDepartment) {
	System.out.println("The rest amount of department ATMs is: " + atmDepartment.getRest());
    }
}
