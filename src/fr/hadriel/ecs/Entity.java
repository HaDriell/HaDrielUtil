package fr.hadriel.ecs;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 21/11/2016.
 */
public final class Entity {

    private List<Component> components;
    public final long id;

    public Entity(long id) {
        this.id = id;
        this.components = new ArrayList<>();
    }

    private Component find(Class clazz) {
        for(Component c : components) {
            if(clazz.isInstance(c)) return c;
        }
        return null;
    }

    public <T extends Component> T get(Class<T> type) {
        Component c = find(type);
        return c == null ? null : type.cast(c);
    }

    public void add(Component component) {
        if(find(component.getClass()) == null)
            components.add(component);
    }

    public void remove(Component component) {
        components.remove(component);
    }

    public void add(Class<? extends Component> componentClass) {
        try {
            add(componentClass.newInstance());
        } catch (Exception ignore) {}
    }

    public void remove(Class<? extends Component> componentClass) {
        try {
            remove(get(componentClass));
        } catch (Exception ignore) {}
    }

    public List<Component> getComponents() {
        return components;
    }
}