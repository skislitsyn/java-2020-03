package skislitsyn;

import skislitsyn.logging.Log;
import skislitsyn.logging.LoggableCalculator;

public class TestWithLogging implements LoggableCalculator {
    @Log
    @Override
    public void calculation(int param) {
	System.out.println("Calculating with param: " + String.valueOf(param));
    };
}