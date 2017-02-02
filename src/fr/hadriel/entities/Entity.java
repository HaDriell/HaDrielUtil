package fr.hadriel.entities;



import fr.hadriel.util.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 21/11/2016.
 */
public final class Entity {

    private Map<String, Property<Object>> properties;
    public long id;

    public Entity(long id) {
        this.id = id;
        this.properties = new HashMap<>();
    }

    public void clear() {
        properties.forEach((k, v) -> v.clearCallbacks());
        properties.clear();
    }

    public Object get(String name) {
        Property<Object> p = properties.get(name);
        return p == null ? null : p.get();
    }

    public <T> T get(String name, Class<T> type) {
        Object o = get(name);
        return type.isInstance(o) ? type.cast(o) : null;
    }

    public void remove(String name) {
        properties.remove(name);
    }

    public boolean hasProperty(String name) {
        return properties.get(name) != null;
    }

    public void set(String name, Object value) {
        Property<Object> p =  properties.get(name);
        if(p == null) {
            p = new Property<>(value);
            properties.put(name, p);
        } else {
            p.set(value);
        }
    }
}