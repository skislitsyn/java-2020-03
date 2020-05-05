package skislitsyn.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;

import skislitsyn.logging.Log;

public class Ioc {
    private Ioc() {
    }

    public static Object createMyClass(Object obj) {
	InvocationHandler handler = new MyInvocationHandler(obj);
	return Proxy.newProxyInstance(Ioc.class.getClassLoader(), obj.getClass().getInterfaces(), handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
	private final Object myClass;
	private final HashMap<Class<?>, HashSet<Method>> loggableInterfaces = new HashMap<>();

	MyInvocationHandler(Object myClass) {
	    this.myClass = myClass;
	    Method[] methods = myClass.getClass().getDeclaredMethods();
	    for (Method method : methods) {
		if (method.isAnnotationPresent(Log.class)) {
		    Class<?>[] interfaces = myClass.getClass().getInterfaces();
		    for (Class<?> i : interfaces) {
			try {
			    Method interfaceMethod = i.getDeclaredMethod(method.getName(), method.getParameterTypes());
			    HashSet<Method> methodsSet = null;
			    if (loggableInterfaces.containsKey(i)) {
				methodsSet = loggableInterfaces.get(i);
			    } else {
				methodsSet = new HashSet<>();
			    }
			    methodsSet.add(interfaceMethod);
			    loggableInterfaces.put(i, methodsSet);
			} catch (NoSuchMethodException e) {
			    // Interface do not have the method with annotation Log
			} catch (SecurityException e) {
			    throw new RuntimeException(e.getMessage());
			}
		    }
		}
	    }
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    if (loggableInterfaces.get(method.getDeclaringClass()) != null
		    && loggableInterfaces.get(method.getDeclaringClass()).contains(method)) {
		System.out.println("executed method: " + method.getName() + ", param: " + getPrintableArgs(args));
	    }
	    return method.invoke(myClass, args);
	}

	@Override
	public String toString() {
	    return "MyInvocationHandler{" + "myClass=" + myClass + '}';
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