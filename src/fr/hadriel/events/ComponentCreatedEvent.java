package fr.hadriel.events;

import fr.hadriel.ecs.Component;
import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 24/11/2016.
 */
public class ComponentCreatedEvent implements IEvent {

    public final long id;
    public final Class<? extends Component> componentClass;

    public ComponentCreatedEvent(long id, Class<? extends Component> componentClass) {
        this.id = id;
        this.componentClass = componentClass;
    }
}