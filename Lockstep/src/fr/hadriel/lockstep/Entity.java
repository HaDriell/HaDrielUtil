package fr.hadriel.lockstep;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Entity {

    public static final int STABLE    = 0b000; // entity is the same (signature-wise)
    public static final int CREATED   = 0b001; // entity was created
    public static final int MODIFIED  = 0b010; // component signature changed
    public static final int DESTROYED = 0b100; // entity removed

    public final long id;
    private int flag;
    private Map<Class<? extends IComponent>, IComponent> components;

    public Entity(long id, IComponent... defaultComponents) {
        this.id = id;
        this.components = new HashMap<>();
        this.flag = CREATED;
        if(defaultComponents != null)
            Arrays.stream(defaultComponents).forEach(this::setComponent);
    }

    void clearFlags() {
        flag = STABLE;
    }

    /**
     * an Entity is Stable if its signature (the component map) has not changed
     * @return true if the Entity is unstable
     */
    public boolean isUnstable() {
        return flag != STABLE;
    }

    /**
     * an Entity is Destroyed when a {@link Entity::destroy} method is called.
     * @return true if the Entity has been destroyed
     */
    public boolean isDestroyed() {
        return (flag & DESTROYED) != STABLE;
    }

    public boolean isCreated() {
        return (flag & CREATED) != STABLE;
    }

    public boolean isModified() {
        return (flag & MODIFIED) != STABLE;
    }

    public void destroy() {
        flag |= DESTROYED;
    }

    public <T extends IComponent> boolean hasComponent(Class<T> type) {
        Object o = components.get(type);
        return type.isInstance(o);
    }

    public <T extends IComponent> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    public <T extends IComponent> void setComponent(T component) {
        Objects.requireNonNull(component);
        Object old = components.put(component.getClass(), component);
        if(old == null) flag |= MODIFIED; // new component in signature
    }

    public <T extends IComponent> T removeComponent(Class<T> type) {
        T c = type.cast(components.remove(type));
        if(c != null) flag |= MODIFIED; // the component has really been removed
        return c;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Entity ").append(id).append(" ");
        components.forEach((type, component) -> out.append("[").append(type.getSimpleName()).append(": ").append(component).append("]"));
        return out.toString();
    }
}