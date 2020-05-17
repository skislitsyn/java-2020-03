package skislitsyn.impl;

import java.util.ArrayList;
import java.util.List;

import skislitsyn.Banknote;
import skislitsyn.CashBasket;
import skislitsyn.FaceValue;

public class CashBasketImpl implements CashBasket {
    private static final int MAX_QUANTITY = 100;
    private final FaceValue faceValue;
    private int currentQuantity = 0;

    public CashBasketImpl(FaceValue faceValue) {
	this.faceValue = faceValue;
    }

    @Override
    public void loadBanknotes(List<Banknote> banknotes) {
	currentQuantity += banknotes.size();
    }

    @Override
    public List<Banknote> getBanknotes(int quantity) {
	List<Banknote> banknotesRequested = new ArrayList<>();
	for (int i = 0; i < quantity; i++) {
	    banknotesRequested.add(new BanknoteImpl(faceValue));
	}
	return banknotesRequested;
    }

    @Override
    public FaceValue getFaceValue() {
	return faceValue;
    }

    @Override
    public int getCurrentQuantity() {
	return currentQuantity;
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
