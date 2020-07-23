package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
	extractMetadataFromAppConfig(configClass, methodsOrderMap, methodNameMap);

	createAppComponents(configClass, methodsOrderMap, methodNameMap);
    }

    private void checkConfigClass(Class<?> configClass) {
	if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
	    throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
	return (C) searchAppComponentByClass(componentClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
	return (C) appComponentsByName.get(componentName);
    }

    private void extractMetadataFromAppConfig(Class<?> configClass, Map<Integer, List<Method>> methodsOrderMap,
	    Map<Method, String> methodNameMap) {
	Method[] methods = configClass.getDeclaredMethods();
	for (Method method : methods) {
	    if (method.isAnnotationPresent(AppComponent.class)) {
		AppComponent appComponent = method.getAnnotation(AppComponent.class);

		int order = appComponent.order();
		if (!methodsOrderMap.containsKey(order)) {
		    methodsOrderMap.put(order, new ArrayList<>());
		}
		methodsOrderMap.get(order).add(method);

		String name = appComponent.name();
		methodNameMap.put(method, name);
	    }
	}
    }

    private void createAppComponents(Class<?> configClass, Map<Integer, List<Method>> methodsOrderMap,
	    Map<Method, String> methodNameMap) {
	try {
	    Object obj = configClass.getConstructor().newInstance();

	    for (Integer order : methodsOrderMap.keySet()) {
		List<Method> methodsOfTheOrder = methodsOrderMap.get(order);
		for (Method methodOfTheOrder : methodsOfTheOrder) {
		    Object appComponent = createAppComponent(obj, methodOfTheOrder);
		    appComponents.add(appComponent);
		    appComponentsByName.put(methodNameMap.get(methodOfTheOrder), appComponent);
		}
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private Object createAppComponent(Object obj, Method method) throws ReflectiveOperationException {
	Class<?>[] paramTypes = method.getParameterTypes();
	Object[] args = searchMethodArguments(paramTypes);
	return method.invoke(obj, args);
    }

    private Object[] searchMethodArguments(Class<?>[] paramTypes) {
	List<Object> appropriateAppComponents = new ArrayList<>();

	for (Class<?> paramType : paramTypes) {
	    appropriateAppComponents.add(searchAppComponentByClass(paramType));
	}

	return appropriateAppComponents.toArray();
    }

    private Object searchAppComponentByClass(Class<?> componentClass) {
	List<Object> componentsFound = appComponents.stream()
		.filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
		.collect(Collectors.toList());
	if (componentsFound.size() == 1) {
	    return componentsFound.get(0);
	} else {
	    if (componentsFound.size() == 0) {
		throw new RuntimeException("No component found by class " + componentClass.getCanonicalName());
	    } else {
		throw new RuntimeException(
			"More than one component found by class " + componentClass.getCanonicalName());
	    }
	}
    }

}
