package fr.hadriel.entities.events;

import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class DestroyEntityEvent implements IEvent {

    public final long id;

    public DestroyEntityEvent(long id) {
        this.id = id;
    }
}
