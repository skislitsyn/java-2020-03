package skislitsyn;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyArrayList<T> implements List<T> {
    private int size;
    private int lastElementIndex;
    private Object[] container;
    private final static int CONTAINER_INITIAL_SIZE = 10;
    private final static int CONTAINER_RESIZE_MULTIPLIER = 2;

    public MyArrayList() {
	size = CONTAINER_INITIAL_SIZE;
	lastElementIndex = -1;
	container = new Object[size];
    }

    @Override
    public boolean add(T e) {
	lastElementIndex++;

	if (lastElementIndex == size - 1) {
	    resizeContainer();
	}

	container[lastElementIndex] = e;

	return true;
    }

    @Override
    public int size() {
	return lastElementIndex + 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
	if (index < 0 || index > lastElementIndex) {
	    throw new IllegalArgumentException("Index out of bound");
	} else {
	    return (T) container[index];
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
	if (index < 0 || index > lastElementIndex) {
	    throw new IllegalArgumentException("Index out of bound");
	} else {
	    T previousElement = (T) container[index];
	    container[index] = element;
	    return previousElement;
	}
    }

    @Override
    public Object[] toArray() {
	Object[] result = new Object[size()];
	System.arraycopy(container, 0, result, 0, size());
	return result;
    }

    @Override
    public ListIterator<T> listIterator() {
	return new MyArrayListListIterator();
    }

    @Override
    public Iterator<T> iterator() {
	return new MyArrayListIterator();
    }

    private void resizeContainer() {
	size *= CONTAINER_RESIZE_MULTIPLIER;
	Object[] resizedContainer = new Object[size];
	System.arraycopy(container, 0, resizedContainer, 0, container.length);
	container = resizedContainer;
    }

    private class MyArrayListListIterator implements ListIterator<T> {
	private int currentElementIndex;

	MyArrayListListIterator() {
	    currentElementIndex = -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
	    currentElementIndex++;
	    return (T) MyArrayList.this.container[currentElementIndex];
	}

	@Override
	public void set(T e) {
	    MyArrayList.this.container[currentElementIndex] = e;
	}

	@Override
	public boolean hasNext() {
	    throw new UnsupportedOperationException("Method is not implemented");

	}

	@Override
	public boolean hasPrevious() {
	    throw new UnsupportedOperationException("Method is not implemented");

	}

	@Override
	public T previous() {
	    throw new UnsupportedOperationException("Method is not implemented");

	}

	@Override
	public int nextIndex() {
	    throw new UnsupportedOperationException("Method is not implemented");

	}

	@Override
	public int previousIndex() {
	    throw new UnsupportedOperationException("Method is not implemented");

	}

	@Override
	public void remove() {
	    throw new UnsupportedOperationException("Method is not implemented");

	}

	@Override
	public void add(T e) {
	    throw new UnsupportedOperationException("Method is not implemented");

	}
    }

    private class MyArrayListIterator implements Iterator<T> {
	private int currentElementIndex;

	MyArrayListIterator() {
	    currentElementIndex = -1;
	}

	@Override
	public boolean hasNext() {
	    return currentElementIndex < MyArrayList.this.lastElementIndex ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
	    currentElementIndex++;
	    return (T) MyArrayList.this.container[currentElementIndex];
	}

    }

    @Override
    public boolean isEmpty() {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean contains(Object o) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public <T> T[] toArray(T[] a) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean remove(Object o) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean containsAll(Collection<?> c) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean removeAll(Collection<?> c) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public boolean retainAll(Collection<?> c) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public void clear() {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public void add(int index, T element) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public T remove(int index) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public int indexOf(Object o) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public int lastIndexOf(Object o) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public ListIterator<T> listIterator(int index) {
	throw new UnsupportedOperationException("Method is not implemented");

    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
	throw new UnsupportedOperationException("Method is not implemented");

    };

}
