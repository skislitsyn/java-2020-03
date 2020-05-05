package skislitsyn;

import skislitsyn.logging.LoggableCalculator;
import skislitsyn.logging.LoggablePrinter;
import skislitsyn.utils.Ioc;

public class TestLoggingApp {

    public static void main(String[] args) {
	System.out.println("Launch test with logging:");
	LoggableCalculator testWithLogging = (LoggableCalculator) Ioc.createMyClass(new TestWithLogging());
	testWithLogging.calculation(1);
	testWithLogging.calculation(2);
	testWithLogging.calculation(3);

	System.out.println("Launch test without logging:");
	LoggableCalculator testWithOutLogging = (LoggableCalculator) Ioc.createMyClass(new TestWithOutLogging());
	testWithOutLogging.calculation(1);
	testWithOutLogging.calculation(2);
	testWithOutLogging.calculation(3);

	System.out.println("Launch another test with logging:");
	LoggablePrinter anotherTestWithLogging = (LoggablePrinter) Ioc.createMyClass(new AnotherTestWithLogging());
	anotherTestWithLogging.print(String.valueOf(4));
	anotherTestWithLogging.print(String.valueOf(5));
	anotherTestWithLogging.print(String.valueOf(6));

	System.out.println("Launch another test without logging:");
	LoggablePrinter anotherTestWithOutLogging = (LoggablePrinter) Ioc
		.createMyClass(new AnotherTestWithOutLogging());
	anotherTestWithOutLogging.print(String.valueOf(4));
	anotherTestWithOutLogging.print(String.valueOf(5));
	anotherTestWithOutLogging.print(String.valueOf(6));
    }

}
