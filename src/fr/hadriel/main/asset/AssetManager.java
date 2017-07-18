package fr.hadriel.main.asset;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 20/02/2017.
 */
public class AssetManager {

    private Map<Class, InstanceManager> managers;

    public AssetManager() {
        managers = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> InstanceManager<T> getInstanceManager(Class<T> type) {
        InstanceManager im = managers.get(type);
        if(im == null) {
            im = new InstanceManager<T>();
            managers.put(type, im);
        }
        return im;
    }

    public synchronized <T> T get(Class<T> type, int id) {
        InstanceManager<T> im = getInstanceManager(type);
        return im == null ? null : im.get(id);
    }

    public synchronized <T> int load(Class<T> type, T instance) {
        InstanceManager<T> im = getInstanceManager(type);
        return im.load(instance);
    }

    public synchronized <T> void unload(Class<T> type, int id) {
        InstanceManager<T> im = getInstanceManager(type);
        im.unload(id);
    }
}