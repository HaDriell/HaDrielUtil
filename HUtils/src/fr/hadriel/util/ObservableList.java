package fr.hadriel.util;

import java.util.*;

public class ObservableList<T> implements Iterable<T> {

    private final List<T> backend;
    private List<Callback<List<T>>> observers;

    public ObservableList() {
        this(new ArrayList<>());
    }

    public ObservableList(List<T> backendList) {
        this.backend = Objects.requireNonNull(backendList);
        this.observers = new ArrayList<>();
    }

    private void __notify() {
        List<T> snapshot = get();
        observers.forEach(obs -> obs.execute(snapshot));
    }

    public void clear() {
        if (backend.isEmpty())
            return;
        backend.clear();
        __notify();
    }

    public List<T> get() {
        return Collections.unmodifiableList(backend);
    }

    public void addObserver(Callback<List<T>> callback) {
        observers.add(callback);
    }

    public void removeObserver(Callback<List<T>> callback) {
        observers.remove(callback);
    }

    public boolean contains(T element) {
        return backend.contains(element);
    }

    public void add(T elements) {
        if (backend.add(elements)) __notify();
    }

    public void remove(T elements) {
        if (backend.remove(elements)) __notify();
    }

    public void add(Collection<T> elements) {
        if (backend.addAll(elements)) __notify();
    }

    public void remove(Collection<T> element) {
        if (backend.removeAll(element)) __notify();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private final List<T> list = get(); // unmodifiable snapshot
            private int i = 0;

            public boolean hasNext() {
                return i < list.size();
            }

            public T next() {
                return list.get(i++);
            }
        };
    }

    public Iterator<T> iteratorInverse() {
        return new Iterator<T>() {

            private final List<T> list = get(); // unmodifiable snapshot
            private int i = list.size() - 1;

            public boolean hasNext() {
                return i > 0;
            }

            public T next() {
                return list.get(i--);
            }
        };
    }
}