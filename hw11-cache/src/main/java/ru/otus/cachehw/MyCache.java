package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergey created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    private final static Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> container = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    public MyCache() {

    }

    @Override
    public void put(K key, V value) {
	container.put(key, value);
	notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
	V v = container.get(key);
	container.remove(key);
	notify(key, v, "remove");
    }

    @Override
    public V get(K key) {
	V v = container.get(key);
	notify(key, v, "get");
	return v;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
	listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
	listeners.remove(listener);
    }

    private void notify(K key, V value, String action) {
	for (HwListener<K, V> hwListener : listeners) {
	    try {
		hwListener.notify(key, value, action);
	    } catch (Exception e) {
		logger.error("Error '{}' occured while sending notify to listener {}", e.getMessage(), hwListener);
	    }
	}
    }
}
