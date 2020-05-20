package skislitsyn.impl;

import skislitsyn.Banknote;
import skislitsyn.Nominal;

public class BanknoteImpl implements Banknote {
    private final Nominal nominal;

    public BanknoteImpl(Nominal nominal) {
	this.nominal = nominal;
    }

    public BanknoteImpl(int value) {
	this.nominal = Nominal.getFromValue(value);
    }

    @Override
    public Nominal getNominal() {
	return nominal;
    }

    @Override
    public String toString() {
	return "BanknoteImpl{" + "nominal=" + nominal + '}';
    }
}
