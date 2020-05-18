package skislitsyn.impl;

import java.util.ArrayList;
import java.util.List;

import skislitsyn.Banknote;
import skislitsyn.CashBasket;
import skislitsyn.Nominal;

public class CashBasketImpl implements CashBasket {
    private static final int MAX_QUANTITY = 100;
    private final Nominal nominal;
    private int currentQuantity = 0;

    public CashBasketImpl(Nominal nominal) {
	this.nominal = nominal;
    }

    public CashBasketImpl(Nominal nominal, int currentQuantity) {
	this.nominal = nominal;
	this.currentQuantity = currentQuantity;
    }

    @Override
    public void loadBanknotes(List<Banknote> banknotes) {
	currentQuantity += banknotes.size();
    }

    @Override
    public List<Banknote> getBanknotes(int quantity) {
	List<Banknote> banknotesRequested = new ArrayList<>();
	for (int i = 0; i < quantity; i++) {
	    banknotesRequested.add(new BanknoteImpl(nominal));
	    currentQuantity--;
	}
	return banknotesRequested;
    }

    @Override
    public Nominal getNominal() {
	return nominal;
    }

    @Override
    public int getCurrentQuantity() {
	return currentQuantity;
    }

    @Override
    public boolean isLoadAvailable(int quantity) {
	return currentQuantity + quantity <= MAX_QUANTITY;
    }

    @Override
    public boolean isAvailable(int quantity) {
	return quantity <= currentQuantity;
    }

    @Override
    public boolean isFull() {
	return currentQuantity == MAX_QUANTITY;
    }
}
