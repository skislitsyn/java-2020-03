package skislitsyn;

public class DigitPrinter {
    private int digit;
    private final int incrementIndicator = 1;
    private final int decrementIndicator = 10;
    private Operation operation = Operation.INCREMENT;

    public void printNext() {
	applyOperation();
	System.out.println(digit);
    }

    private void applyOperation() {
	switch (operation) {
	case INCREMENT:
	    digit++;
	    break;
	case DECREMENT:
	    digit--;
	    break;
	default:
	    break;
	}
	switchOperation();
    }

    private void switchOperation() {
	switch (digit) {
	case 1: {
	    operation = Operation.INCREMENT;
	    break;
	}
	case 10: {
	    operation = Operation.DECREMENT;
	    break;
	}
	default:
	    break;
	}
    }
}
