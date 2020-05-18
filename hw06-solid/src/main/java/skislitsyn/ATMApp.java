package skislitsyn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import skislitsyn.impl.ATMImpl;
import skislitsyn.impl.BanknoteImpl;

public class ATMApp {
    private static final int BANKNOTE_BATCH_SIZE = 100;

    public static void main(String[] args) {
	// Prepare ATM
	Nominal[] atmNominalsAvailable = { Nominal.N5000, Nominal.N1000, Nominal.N500 };
	ATM atm = new ATMImpl(new HashSet<>(Arrays.asList(atmNominalsAvailable)));

	// Load banknotes
	loadBanknotes(atm, atmNominalsAvailable);

	// Normally get some amount
	getAmount(atm, 14500);
	getRest(atm);

	// Get some invalid (low) amount
	getAmount(atm, 400);
	getRest(atm);

	// Get some invalid (high) amount
	getAmount(atm, 636000);
	getRest(atm);

    }

    private static void loadBanknotes(ATM atm, Nominal[] atmNominalsAvailable) {
	List<Banknote> banknotesForLoad = new ArrayList<>();
	for (Nominal nominal : atmNominalsAvailable) {
	    banknotesForLoad.addAll(getBanknotesForLoad(nominal, BANKNOTE_BATCH_SIZE));
	}
	try {
	    atm.doCashIn(banknotesForLoad);
	    System.out.println("Amount loaded to the ATM: " + atm.getRest());
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

    private static void getAmount(ATM atm, long amountRequested) {
	try {
	    System.out.println("Amount requested: " + amountRequested);
	    List<Banknote> cash = atm.doCashOut(amountRequested);
	    System.out.println("Cash received from the ATM: " + cash);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static void getRest(ATM atm) {
	System.out.println("The rest amount of the ATM is: " + atm.getRest());
    }
}
