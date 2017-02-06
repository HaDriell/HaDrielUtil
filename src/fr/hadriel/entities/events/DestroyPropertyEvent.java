package fr.hadriel.entities.events;

import fr.hadriel.event.Event;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class DestroyPropertyEvent extends Event {
    public long id;
    public String name;

    public DestroyPropertyEvent(long id, String name) {
        super(true);
        this.id = id;
        this.name = name;
    }
}