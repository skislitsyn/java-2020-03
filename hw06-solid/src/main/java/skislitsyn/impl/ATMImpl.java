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
import java.util.TreeMap;

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
    private static final long serialVersionUID = 1L;
    private final Map<Integer, Nominal> nominalsAvailable = new TreeMap<>(Comparator.reverseOrder());
    private final Set<CashBasket> cashBacketsAvailable = new HashSet<>();
    private final Map<Nominal, CashBasket> nominalsCashBacketsMap = new HashMap<>();

    public ATMImpl(Set<Nominal> nominalsAvailable) {
	for (Nominal nominal : nominalsAvailable) {
	    this.nominalsAvailable.put(nominal.getValue(), nominal);
	    CashBasket cashBasket = new CashBasketImpl(nominal);
	    cashBacketsAvailable.add(cashBasket);
	    nominalsCashBacketsMap.put(nominal, cashBasket);
	}
    }

    @Override
    public void doCashIn(List<Banknote> banknotes) throws IOException {
	Map<Nominal, Integer> banknotesForLoad = new HashMap<>();

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

	if (cashOutOptions.isEmpty()) {
	    throw new IOException("Amount requested can not be cashed out: no proper banknote nominal");
	}

	boolean cashOutAvailable = false;
	for (Integer optionId : cashOutOptions.keySet()) {
	    if (isCashOutAvailable(cashOutOptions.get(optionId))) {
		return getCash(cashOutOptions.get(optionId));
	    }
	}

	if (!cashOutAvailable) {
	    throw new IOException("Amount requested can not be cashed out: amount requested is too high");
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
	if (!nominalsAvailable.values().contains(banknote.getNominal())) {
	    throw new IOException("Banknotes with this nominal can not be cashed in");
	}
    }

    private void addBanknoteForLoad(Map<Nominal, Integer> banknotesForLoad, Banknote banknote) {
	int currentQuantity = banknotesForLoad.containsKey(banknote.getNominal())
		? banknotesForLoad.get(banknote.getNominal())
		: 0;
	banknotesForLoad.put(banknote.getNominal(), currentQuantity + 1);
    }

    private void checkLoadAvailable(Map<Nominal, Integer> banknotesForLoad) throws IOException {
	for (Nominal nominal : banknotesForLoad.keySet()) {
	    if (!nominalsCashBacketsMap.get(nominal).isLoadAvailable(banknotesForLoad.get(nominal))) {
		throw new IOException("Banknotes can not be loaded");
	    }
	}
    }

    private void load(Map<Nominal, Integer> banknotesForLoad) {
	for (Nominal nominal : banknotesForLoad.keySet()) {
	    nominalsCashBacketsMap.get(nominal).loadBanknotes(banknotesForLoad.get(nominal));
	}
    }

    /*
     * Получаем варианты выдачи наличных, разложенные в зависимости от доступной
     * купюры максимального номинала. Перебираем все доступные номиналы купюр,
     * начиная с наибольшего, и в каждой итерации спускаемся к следующему меньшему
     * номиналу.
     */
    private SortedMap<Integer, Map<Nominal, Integer>> getCashOutOptions(long amountRequested) throws IOException {
	SortedMap<Integer, Map<Nominal, Integer>> cashOutOptions = new TreeMap<>();
	List<Integer> nominalsValues = new ArrayList<>();
	nominalsValues.addAll(nominalsAvailable.keySet());

	int optionId = 0;
	int optionsCount = nominalsAvailable.size();
	for (int i = 0; i < optionsCount; i++) {
	    int highestNominal = nominalsValues.get(0);
	    if (amountRequested >= highestNominal) {
		Map<Nominal, Integer> cashOutOption = getCashOutOption(amountRequested, nominalsValues);
		if (cashOutOption != null) {
		    cashOutOptions.put(optionId, cashOutOption);
		    optionId++;
		}
	    }
	    nominalsValues.remove(0);
	}

	return cashOutOptions;
    }

    /*
     * Пытаемся разложить сумму по купюрам доступного номинала. Если не получилось,
     * возвращаем null
     */
    private Map<Nominal, Integer> getCashOutOption(long amountRequested, List<Integer> nominalsValues)
	    throws IOException {
	Map<Nominal, Integer> nominalsQuantityMap = new HashMap<>();
	long amountRequestedRest = amountRequested;
	for (Integer nominalsValue : nominalsValues) {
	    nominalsQuantityMap.put(Nominal.getFromValue(nominalsValue), (int) (amountRequestedRest / nominalsValue));
	    amountRequestedRest %= nominalsValue;
	    if (amountRequestedRest == 0) {
		return nominalsQuantityMap;
	    }
	}
	return null;
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
	    result.addAll(getBanknotes(nominal, nominalsCashBacketsMap.get(nominal).getBanknotes(option.get(nominal))));
	}
	return result;
    }

    private List<Banknote> getBanknotes(Nominal nominal, int quantuty) {
	List<Banknote> result = new ArrayList<>();
	Banknote banknote = new BanknoteImpl(nominal);
	for (int i = 0; i < quantuty; i++) {
	    result.add(banknote);
	}
	return result;
    }
}
