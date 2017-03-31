package fr.hadriel.ecs.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class ComponentTypeChangedEvent implements IEvent {
    public final long id;
    public final String name;
    public final Object component;

    public ComponentTypeChangedEvent(long id, String name, Object component) {
        this.id = id;
        this.name = name;
        this.component = component;
    }
}
