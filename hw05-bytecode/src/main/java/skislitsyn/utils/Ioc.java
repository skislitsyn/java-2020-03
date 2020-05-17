package skislitsyn.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import skislitsyn.logging.Log;

public class Ioc {
    private Ioc() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T createMyProxy(T obj) {
	InvocationHandler handler = new InvocationHandlerImpl(obj);
	return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), obj.getClass().getInterfaces(), handler);
    }

    static class InvocationHandlerImpl implements InvocationHandler {
	private final Object obj;
	private final Set<Method> loggableMethods = new HashSet<>();

	InvocationHandlerImpl(Object obj) {
	    this.obj = obj;
	    Method[] methods = obj.getClass().getDeclaredMethods();
	    for (Method method : methods) {
		if (method.isAnnotationPresent(Log.class)) {
		    Class<?>[] interfaces = obj.getClass().getInterfaces();
		    for (Class<?> i : interfaces) {
			try {
			    Method interfaceMethod = i.getDeclaredMethod(method.getName(), method.getParameterTypes());
			    loggableMethods.add(interfaceMethod);
			} catch (NoSuchMethodException e) {
			    // Interface do not have the method with annotation Log
			} catch (Exception e) {
			    throw new RuntimeException(e);
			}
		    }
		}
	    }
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    if (loggableMethods.contains(method)) {
		System.out.println("executed method: " + method.getName() + ", param: " + getPrintableArgs(args));
	    }
	    return method.invoke(obj, args);
	}

	@Override
	public String toString() {
	    return "MyInvocationHandler{" + "myClass=" + obj + '}';
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