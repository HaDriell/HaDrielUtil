package fr.hadriel.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 02/01/2017.
 */
public class FocusChangedEvent implements IEvent {

    public final boolean active;

    public FocusChangedEvent(boolean active) {
        this.active = active;
    }
}