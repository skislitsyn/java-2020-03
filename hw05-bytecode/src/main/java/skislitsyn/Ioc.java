package skislitsyn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import skislitsyn.logging.Log;
import skislitsyn.logging.LoggableCalculator;

class Ioc {
    private Ioc() {
    }

    static LoggableCalculator createMyClass() {
	InvocationHandler handler = new MyInvocationHandler(new TestLogging());
	return (LoggableCalculator) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
		new Class<?>[] { LoggableCalculator.class }, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
	private final LoggableCalculator myClass;

	MyInvocationHandler(LoggableCalculator myClass) {
	    this.myClass = myClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    Method myClassMethod = myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
	    if (myClassMethod.isAnnotationPresent(Log.class)) {
		System.out.println("executed method: " + method.getName() + ", param: " + getPrintableArgs(args));
	    }
	    return method.invoke(myClass, args);
	}

	@Override
	public String toString() {
	    return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
	}
    }

    private static String getPrintableArgs(Object[] args) {
	String result = "";
	for (Object arg : args) {
	    result += arg.toString() + ", ";
	}
	return result.replaceAll(", $", "");
    }
}