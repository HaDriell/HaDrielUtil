package fr.hadriel.events;

import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 24/11/2016.
 */
public class EntityCreatedEvent implements IEvent {

    public final long id;

    public EntityCreatedEvent(long id) {
        this.id = id;
    }
}