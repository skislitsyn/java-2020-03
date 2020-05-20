package skislitsyn.impl;

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
    public void loadBanknotes(int quantity) {
	currentQuantity += quantity;
    }

    @Override
    public int getBanknotes(int quantity) {
	currentQuantity -= quantity;
	return quantity;
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
