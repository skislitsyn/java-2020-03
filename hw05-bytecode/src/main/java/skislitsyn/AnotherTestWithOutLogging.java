package skislitsyn;

import skislitsyn.logging.LoggablePrinter;

public class AnotherTestWithOutLogging implements LoggablePrinter {
    @Override
    public void print(String s) {
	System.out.println("Printing param: " + s);
    }
}
