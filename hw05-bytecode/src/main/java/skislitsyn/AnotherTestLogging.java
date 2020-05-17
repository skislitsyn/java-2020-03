package skislitsyn;

import skislitsyn.logging.Log;
import skislitsyn.logging.LoggablePrinter;

public class AnotherTestLogging implements LoggablePrinter {
    @Log
    @Override
    public void print(String s) {
	System.out.println("Printing param: " + s);
    }

    @Override
    public void printWithoutAnnotation(String s) {
	System.out.println("Printing param: " + s);
    }
}
