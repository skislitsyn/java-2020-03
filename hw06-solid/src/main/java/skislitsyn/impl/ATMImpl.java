package skislitsyn.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import skislitsyn.ATM;
import skislitsyn.Banknote;
import skislitsyn.CashBasket;
import skislitsyn.FaceValue;

public class ATMImpl implements ATM {
    private final CashBasket n5000CashBasket = new CashBasketImpl(FaceValue.N5000);
    private final CashBasket n1000CashBasket = new CashBasketImpl(FaceValue.N1000);
    private final CashBasket n500CashBasket = new CashBasketImpl(FaceValue.N500);
    private final Set<CashBasket> cashBackets = new HashSet<>();

    public ATMImpl() {
	cashBackets.add(n5000CashBasket);
	cashBackets.add(n1000CashBasket);
	cashBackets.add(n500CashBasket);
    }

    @Override
    public void doCashIn(List<Banknote> banknotes) {
	Map<CashBasket, List<Banknote>> cashBasketsForLoad = new HashMap<>();

	for (Banknote banknote : banknotes) {
	    switch (banknote.getFaceValue()) {
	    case N5000: {
		addBanknoteForLoad(cashBasketsForLoad, n5000CashBasket, banknote);
	    }
	    case N1000: {
		addBanknoteForLoad(cashBasketsForLoad, n1000CashBasket, banknote);
	    }
	    case N500: {
		addBanknoteForLoad(cashBasketsForLoad, n500CashBasket, banknote);
	    }
	    default:
		throw new IllegalArgumentException("Unexpected value: " + banknote.getFaceValue());
	    }
	}

	for (CashBasket cashBacket : cashBasketsForLoad.keySet()) {
	    cashBacket.loadBanknotes(cashBasketsForLoad.get(cashBacket));
	}
    }

    @Override
    public List<Banknote> doCashOut(long amountRequested) throws IOException {
	if (amountRequested > getRest()) {
	    throw new IOException("Amount requested can not be give out");
	}
	return null;
    }

    @Override
    public long getRest() {
	long rest = 0;
	for (CashBasket cashBasket : cashBackets) {
	    rest += cashBasket.getFaceValue().getValue() * cashBasket.getCurrentQuantity();
	}
	return rest;
    }

    private void addBanknoteForLoad(Map<CashBasket, List<Banknote>> cashBaskets, CashBasket cashBacket,
	    Banknote banknote) {
	List<Banknote> banknotesForLoad;
	if (cashBaskets.containsKey(cashBacket)) {
	    banknotesForLoad = cashBaskets.get(cashBacket);
	} else {
	    banknotesForLoad = new ArrayList<>();
	}
	banknotesForLoad.add(banknote);
	cashBaskets.put(cashBacket, banknotesForLoad);
    }
}
