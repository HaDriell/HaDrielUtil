package fr.hadriel.main.ecs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class EntityData implements Iterable<Map.Entry<String, Object>> {

    private Map<String, Object> data;

    public EntityData() {
        this.data = new HashMap<>();
    }

    public Iterator<Map.Entry<String, Object>> iterator() {
        return data.entrySet().iterator();
    }

    public Object getComponent(String name) {
        return data.get(name);
    }

    public <T> T getComponent(String name, Class<T> type) {
        Object component = data.get(name);
        return type.isInstance(component) ? type.cast(component) : null;
    }

    public Object setComponent(String name, Object component) {
        //If new Component is null, do a remove instead of setting to null
        return component == null ? data.remove(name) : data.put(name, component);
    }
}