package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
	processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
	checkConfigClass(configClass);

	Map<Integer, List<Method>> methodsOrderMap = new TreeMap<>();
	Map<Method, String> methodNameMap = new HashMap<>();
	Method[] methods = configClass.getDeclaredMethods();
	for (Method method : methods) {
	    if (method.isAnnotationPresent(AppComponent.class)) {
		AppComponent appComponent = method.getAnnotation(AppComponent.class);

		int order = appComponent.order();
		if (methodsOrderMap.containsKey(order)) {
		    methodsOrderMap.get(order).add(method);
		} else {
		    List<Method> methodsOfOneOrder = new ArrayList<>();
		    methodsOfOneOrder.add(method);
		    methodsOrderMap.put(order, methodsOfOneOrder);
		}

		String name = appComponent.name();
		methodNameMap.put(method, name);
	    }
	}

	Object obj = configClass.newInstance();
	for (Integer order : methodsOrderMap.keySet()) {
	    List<Method> methodsOfTheOrder = methodsOrderMap.get(order);
	    for (Method methodOfTheOrder : methodsOfTheOrder) {

	    }
	}
    }

    private void checkConfigClass(Class<?> configClass) {
	if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
	    throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
	}
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
	return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
	return null;
    }

    private int getMethodsSortedByOrder() {

    }
}
