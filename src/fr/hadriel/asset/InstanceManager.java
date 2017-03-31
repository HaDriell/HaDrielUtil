package fr.hadriel.asset;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 20/02/2017.
 */
public class InstanceManager<T> {
    private int sequence;
    private Map<Integer, T> map;
    private T defaultInstance;

    public InstanceManager() {
        this.map = new HashMap<>();
        this.defaultInstance = null;
        this.sequence = 0;
    }

    public void clear() {
        map.clear();
        sequence = 0;
    }

    public synchronized void setDefaultInstance(T defaultInstance) {
        this.defaultInstance = defaultInstance;
    }

    public synchronized T getDefaultInstance() {
        return defaultInstance;
    }

    public synchronized int load(T instance) {
        int id = sequence++;
        map.put(id, instance);
        return id;
    }

    public synchronized void unload(int id) {
        map.remove(id);
    }

    public synchronized T get(int id) {
        return map.getOrDefault(id, defaultInstance);
    }
}