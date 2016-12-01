package fr.hadriel.events;

import fr.hadriel.ecs.Component;
import fr.hadriel.event.IEvent;
import fr.hadriel.serialization.struct.StPrimitive;

/**
 * Created by HaDriel on 24/11/2016.
 */
public class ComponentUpdatedEvent implements IEvent {

    public final long id;
    public final Component component;

    public ComponentUpdatedEvent(long id, Component component) {
        this.id = id;
        this.component = component;
    }
}