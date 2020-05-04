package skislitsyn;

import skislitsyn.logging.LoggableCalculator;

public class TestLoggingApp {

    public static void main(String[] args) {
	LoggableCalculator testLogging = Ioc.createMyClass();
	testLogging.calculation(6);
    }

}
