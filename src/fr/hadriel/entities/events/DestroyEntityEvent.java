package fr.hadriel.entities.events;

import fr.hadriel.event.Event;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class DestroyEntityEvent extends Event {

    public final long id;

    public DestroyEntityEvent(long id) {
        super(true);
        this.id = id;
    }
}
