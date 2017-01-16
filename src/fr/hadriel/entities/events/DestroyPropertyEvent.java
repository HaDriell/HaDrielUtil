package fr.hadriel.entities.events;

import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class DestroyPropertyEvent implements IEvent {
    public long id;
    public String name;

    public DestroyPropertyEvent(long id, String name) {
        this.id = id;
        this.name = name;
    }
}