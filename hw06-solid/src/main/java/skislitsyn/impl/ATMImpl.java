package skislitsyn.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import skislitsyn.ATM;
import skislitsyn.Banknote;
import skislitsyn.CashBasket;
import skislitsyn.Nominal;

/**
 * @author Sergey
 *
 */
/**
 * @author Sergey
 *
 */
public class ATMImpl implements ATM {
    private final Set<Nominal> nominalsAvailable;
    private final Set<CashBasket> cashBacketsAvailable = new HashSet<>();
    private final Map<Nominal, CashBasket> nominalsCashBacketsMap = new HashMap<>();

    public ATMImpl(Set<Nominal> nominalsAvailable) {
	this.nominalsAvailable = nominalsAvailable;
	for (Nominal nominal : nominalsAvailable) {
	    CashBasket cashBasket = new CashBasketImpl(nominal);
	    cashBacketsAvailable.add(cashBasket);
	    nominalsCashBacketsMap.put(nominal, cashBasket);
	}
    }

    @Override
    public void doCashIn(List<Banknote> banknotes) throws IOException {
	Map<Nominal, List<Banknote>> banknotesForLoad = new HashMap<>();

	for (Banknote banknote : banknotes) {
	    checkBanknoteCashInAvailable(banknote);
	    addBanknoteForLoad(banknotesForLoad, banknote);
	}

	checkLoadAvailable(banknotesForLoad);
	load(banknotesForLoad);
    }

    @Override
    public List<Banknote> doCashOut(long amountRequested) throws IOException {
	SortedMap<Integer, Map<Nominal, Integer>> cashOutOptions = getCashOutOptions(amountRequested);

	boolean cashOutAvailable = false;
	for (Integer optionId : cashOutOptions.keySet()) {
	    if (isCashOutAvailable(cashOutOptions.get(optionId))) {
		return getCash(cashOutOptions.get(optionId));
	    }
	}

	if (!cashOutAvailable) {
	    throw new IOException("Amount requested can not be cashed out: cash is over");
	}

	return null;
    }

    @Override
    public long getRest() {
	long rest = 0;
	for (CashBasket cashBasket : cashBacketsAvailable) {
	    rest += cashBasket.getNominal().getValue() * cashBasket.getCurrentQuantity();
	}
	return rest;
    }

    private void checkBanknoteCashInAvailable(Banknote banknote) throws IOException {
	if (!nominalsAvailable.contains(banknote.getNominal())) {
	    throw new IOException("Banknotes with this nominal can not be cashed in");
	}
    }

    private void addBanknoteForLoad(Map<Nominal, List<Banknote>> banknotesForLoad, Banknote banknote) {
	List<Banknote> banknotes;
	if (banknotesForLoad.containsKey(banknote.getNominal())) {
	    banknotes = banknotesForLoad.get(banknote.getNominal());
	} else {
	    banknotes = new ArrayList<>();
	}
	banknotes.add(banknote);
	banknotesForLoad.put(banknote.getNominal(), banknotes);
    }

    private void checkLoadAvailable(Map<Nominal, List<Banknote>> banknotesForLoad) throws IOException {
	try {
	    cashBacketsAvailable.forEach(cashBacket -> {
		if (banknotesForLoad.get(cashBacket.getNominal()) != null) {
		    if (!cashBacket.isLoadAvailable(banknotesForLoad.get(cashBacket.getNominal()).size())) {
			throw new RuntimeException("Banknotes can not be loaded");
		    }
		}
		;
	    });
	} catch (Exception e) {
	    throw new IOException(e);
	}
    }

    private void load(Map<Nominal, List<Banknote>> banknotesForLoad) {
	cashBacketsAvailable.forEach(cashBacket -> {
	    if (banknotesForLoad.get(cashBacket.getNominal()) != null) {
		cashBacket.loadBanknotes(banknotesForLoad.get(cashBacket.getNominal()));
	    }
	    ;
	});
    }

    /*
     * Получаем варианты выдачи наличных, разложенные в зависимости от доступной
     * купюры максимального номинала. Перебираем все доступные номиналы купюр,
     * начиная с наибольшего, и в каждой итерации спускаемся к следующему меньшему
     * номиналу.
     */
    private SortedMap<Integer, Map<Nominal, Integer>> getCashOutOptions(long amountRequested) throws IOException {
	SortedMap<Integer, Map<Nominal, Integer>> cashOutOptions = new TreeMap<>();

	Set<Nominal> nominals = new HashSet<>(nominalsAvailable);
	SortedSet<Integer> nominalsValues = new TreeSet<>(Comparator.reverseOrder());
	for (Nominal nominal : nominals) {
	    nominalsValues.add(nominal.getValue());
	}

	for (int i = 0; i < nominals.size(); i++) {
	    boolean nominalSkipped = false;
	    long amountRequestedRest = amountRequested;
	    Map<Nominal, Integer> nominalsQuantityMap = new HashMap<>();
	    for (Integer nominalsValue : nominalsValues) {
		if (amountRequestedRest >= nominalsValue) {
		    nominalsQuantityMap.put(Nominal.getFromValue(nominalsValue),
			    (int) (amountRequestedRest / nominalsValue));
		    amountRequestedRest %= nominalsValue;
		    if (amountRequestedRest == 0) {
			break;
		    }
		} else {
		    nominalSkipped = true;
		    break;
		}
	    }
	    if (!nominalSkipped) {
		if (amountRequestedRest != 0) {
		    throw new IOException("Amount requested can not be cashed out");
		}
		cashOutOptions.put(cashOutOptions.size(), nominalsQuantityMap);
	    }
	    nominalsValues.remove(nominalsValues.first());
	}

	if (cashOutOptions.isEmpty()) {
	    throw new IOException("Amount requested can not be cashed out");
	}

	return cashOutOptions;
    }

    private boolean isCashOutAvailable(Map<Nominal, Integer> option) {
	for (Nominal nominal : option.keySet()) {
	    if (!nominalsCashBacketsMap.get(nominal).isAvailable(option.get(nominal))) {
		return false;
	    }
	}
	return true;
    }

    private List<Banknote> getCash(Map<Nominal, Integer> option) {
	List<Banknote> result = new ArrayList<>();
	for (Nominal nominal : option.keySet()) {
	    result.addAll(nominalsCashBacketsMap.get(nominal).getBanknotes(option.get(nominal)));
	}
	return result;
    }
}
