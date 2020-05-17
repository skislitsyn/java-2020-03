package skislitsyn.impl;

import skislitsyn.Banknote;
import skislitsyn.FaceValue;

public class BanknoteImpl implements Banknote {
    private final FaceValue faceValue;

    public BanknoteImpl(FaceValue faceValue) {
	this.faceValue = faceValue;
    }

    public BanknoteImpl(int value) {
	this.faceValue = FaceValue.getFromValue(value);
    }

    @Override
    public FaceValue getFaceValue() {
	return faceValue;
    }

    @Override
    public int getValue() {
	return faceValue.getValue();
    }

}
