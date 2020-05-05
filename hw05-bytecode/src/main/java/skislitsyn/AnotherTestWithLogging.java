package skislitsyn;

import skislitsyn.logging.Log;
import skislitsyn.logging.LoggablePrinter;

public class AnotherTestWithLogging implements LoggablePrinter {
    @Log
    @Override
    public void print(String s) {
	System.out.println("Printing param: " + s);
    }
}
