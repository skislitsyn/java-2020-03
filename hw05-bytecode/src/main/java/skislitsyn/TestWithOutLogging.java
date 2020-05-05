package skislitsyn;

import skislitsyn.logging.LoggableCalculator;

public class TestWithOutLogging implements LoggableCalculator {
    @Override
    public void calculation(int param) {
	System.out.println("Calculating with param: " + String.valueOf(param));
    };
}