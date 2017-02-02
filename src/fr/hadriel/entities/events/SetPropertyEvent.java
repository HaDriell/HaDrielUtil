package fr.hadriel.entities.events;

import fr.hadriel.event.IEvent;

/**
 * Created by HaDriel on 13/01/2017.
 */
public class SetPropertyEvent implements IEvent {
    public long id;
    public String name;
    public Object value;

    public SetPropertyEvent(long id, String name, Object value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}