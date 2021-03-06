package skislitsyn.testfw;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyTestEngine {
    private final Class<?> clazz;
    private final ArrayList<String> beforeMethods = new ArrayList<>();
    private final ArrayList<String> afterMethods = new ArrayList<>();
    private final ArrayList<String> testMethods = new ArrayList<>();
    private boolean metadataExtracted = false;
    private int performedTestsCounter = 0;
    private int failedTestsCounter = 0;

    public MyTestEngine(Class<?> clazz) {
	this.clazz = clazz;
    }

    public void doTesting() {
	resetStatistics();
	extractMetadataForTesting();

	for (String testMethod : testMethods) {
	    boolean testMethodInvokationFailed = false;
	    try {
		Object testClassInstance = clazz.getConstructor().newInstance();

		for (String beforeMethod : beforeMethods) {
		    clazz.getMethod(beforeMethod).invoke(testClassInstance);
		}

		try {
		    clazz.getMethod(testMethod).invoke(testClassInstance);
		} catch (Exception e) {
		    testMethodInvokationFailed = true;
		    failedTestsCounter++;
		    printException(e);
		}

		for (String afterMethod : afterMethods) {
		    clazz.getMethod(afterMethod).invoke(testClassInstance);
		}
	    } catch (Exception e) {
		if (!testMethodInvokationFailed) {
		    failedTestsCounter++;
		}
		printException(e);
	    }
	    performedTestsCounter++;
	}

	printTestingStatistics();
    }

    private void extractMetadataForTesting() {
	if (!metadataExtracted) {
	    Method[] methods = clazz.getMethods();
	    for (Method method : methods) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
		    if (annotation instanceof Before) {
			beforeMethods.add(method.getName());
		    } else if (annotation instanceof After) {
			afterMethods.add(method.getName());
		    } else if (annotation instanceof Test) {
			testMethods.add(method.getName());
		    }
		}
	    }
	    metadataExtracted = true;
	}

    }

    private void printTestingStatistics() {
	System.out.println("Testing summary:");
	System.out.println("Number of performed tests: " + Integer.valueOf(getPerformedTestsCount()).toString());
	System.out.println("Number of successfully performed tests: "
		+ Integer.valueOf(getPerformedTestsCount() - getFailedTestsCount()).toString());
	System.out.println("Number of falied tests: " + Integer.valueOf(getFailedTestsCount()).toString());
    }

    private void resetStatistics() {
	performedTestsCounter = 0;
	failedTestsCounter = 0;
    }

    private int getPerformedTestsCount() {
	return performedTestsCounter;
    }

    private int getFailedTestsCount() {
	return failedTestsCounter;
    }

    private void printException(Exception e) {
	if (e instanceof InvocationTargetException) {
	    e.getCause().printStackTrace();
	} else {
	    e.printStackTrace();
	}
    }
}
