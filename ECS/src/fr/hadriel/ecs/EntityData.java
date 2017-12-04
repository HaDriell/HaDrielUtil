package fr.hadriel.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EntityData {

    private final Map<Class, Object> components;

    public EntityData() {
        this.components = new HashMap<>();
    }

    public <T> boolean hasComponent(Class<T> type) {
        Object o = components.get(type);
        return type.isInstance(o);
    }

    public <T> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    public <T> void setComponent(T component) {
        Objects.requireNonNull(component);
        components.put(component.getClass(), component);
    }

    public <T> T removeComponent(Class<T> type) {
        return type.cast(components.remove(type));
    }

    public Map<Class, Object> getComponents() {
        return new HashMap<>(components);
    }
}