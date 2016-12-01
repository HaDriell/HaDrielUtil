package fr.hadriel.events;

import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 24/11/2016.
 */
public class EntityDestroyedEvent implements IEvent {

    public final long id;

    public EntityDestroyedEvent(long id) {
        this.id = id;
    }
}