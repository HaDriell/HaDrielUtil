package fr.hadriel.entities.events;

import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class CreateEntityEvent implements IEvent {

    public final long id;

    public CreateEntityEvent(long id) {
        this.id = id;
    }
}
