package fr.hadriel.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class Property<T> {

    private final List<Callback<T>> callbacks = new ArrayList<>();
    private Callback<T>[] cache;
    private T value;
    private T defaultValue;

    public Property(T value, T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = value == null ? defaultValue : value;
    }

    public Property(T value) {
        this(value, null);
    }

    public Property() {
        this(null, null);
    }

    public synchronized void addCallback(Callback<T> callback) {
        synchronized (callbacks) {
            callbacks.add(callback);
            cache = null;
        }
    }

    public synchronized void removeCallback(Callback<T> callback) {
        synchronized (callbacks) {
            callbacks.remove(callback);
            cache = null;
        }
    }

    public synchronized void clearCallbacks() {
        synchronized (callbacks) {
            callbacks.clear();
            cache = null;
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void doCallback() {
        if(cache == null) {
            cache = new Callback[callbacks.size()];
            callbacks.toArray(cache);
        }
        for(Callback<T> callback : cache) {
            callback.execute(value);
        }
    }

    public void setDefaultValue(T value) {
        this.defaultValue = value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void set(T value) {
        this.value = value == null ? defaultValue : value;
        doCallback();
    }

    public T get() {
        return value;
    }
}
