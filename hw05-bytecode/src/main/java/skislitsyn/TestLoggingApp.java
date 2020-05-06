package skislitsyn;

import skislitsyn.logging.LoggableCalculator;
import skislitsyn.logging.LoggablePrinter;
import skislitsyn.utils.Ioc;

public class TestLoggingApp {

    public static void main(String[] args) {
	System.out.println("Launch test with logging:");
	LoggableCalculator testLogging = Ioc.createMyProxy(new TestLogging());
	testLogging.calculation(1);
	testLogging.calculation(2);
	testLogging.calculation(3);

	System.out.println("Launch test without logging:");
	testLogging.calculationWithoutAnnotation(1);
	testLogging.calculationWithoutAnnotation(2);
	testLogging.calculationWithoutAnnotation(3);

	System.out.println("Launch another test with logging:");
	LoggablePrinter anotherTestLogging = Ioc.createMyProxy(new AnotherTestLogging());
	anotherTestLogging.print(String.valueOf(4));
	anotherTestLogging.print(String.valueOf(5));
	anotherTestLogging.print(String.valueOf(6));

	System.out.println("Launch another test without logging:");
	anotherTestLogging.printWithoutAnnotation(String.valueOf(4));
	anotherTestLogging.printWithoutAnnotation(String.valueOf(5));
	anotherTestLogging.printWithoutAnnotation(String.valueOf(6));
    }

}
