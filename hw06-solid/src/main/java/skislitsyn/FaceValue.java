package skislitsyn;

public enum FaceValue {
    N5000(5000), N2000(2000), N1000(1000), N500(500), N100(100), N50(50), N10(10);

    private int value;

    FaceValue(int value) {
	this.value = value;
    }

    public int getValue() {
	return value;
    }

    public static FaceValue getFromValue(int value) {
	switch (value) {
	case 5000: {
	    return N5000;
	}
	case 2000: {
	    return N2000;
	}
	case 1000: {
	    return N1000;
	}
	case 500: {
	    return N500;
	}
	case 100: {
	    return N100;
	}
	case 50: {
	    return N50;
	}
	case 10: {
	    return N10;
	}
	default:
	    throw new IllegalArgumentException("Unexpected value: " + value);
	}
    }
}
