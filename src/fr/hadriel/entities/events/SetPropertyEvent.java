package fr.hadriel.entities.events;

import fr.hadriel.event.Event;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class SetPropertyEvent extends Event {
    public long id;
    public String name;
    public Object value;

    public SetPropertyEvent(long id, String name, Object value) {
        super(true);
        this.id = id;
        this.name = name;
        this.value = value;
    }
}