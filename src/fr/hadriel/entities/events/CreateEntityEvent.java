package fr.hadriel.entities.events;

import fr.hadriel.event.Event;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class CreateEntityEvent extends Event {

    public final long id;

    public CreateEntityEvent(long id) {
        super(true);
        this.id = id;
    }
}
