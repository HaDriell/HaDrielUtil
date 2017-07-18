package fr.hadriel.main.ecs.events;


/**
 * Created by glathuiliere on 30/03/2017.
 */
public class ComponentTypeChangedEvent implements ECSEvent {
    public final long id;
    public final String name;
    public final Object component;

    public ComponentTypeChangedEvent(long id, String name, Object component) {
        this.id = id;
        this.name = name;
        this.component = component;
    }
}
