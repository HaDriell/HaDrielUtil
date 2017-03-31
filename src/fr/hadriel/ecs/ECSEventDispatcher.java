package fr.hadriel.ecs;

import fr.hadriel.event.EventListener;
import fr.hadriel.event.IEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class ECSEventDispatcher {

    private List<EventListener> listeners;

    public ECSEventDispatcher() {
        this.listeners = new ArrayList<>();
    }

    public synchronized void addEventListener(EventListener listener) {
        if(listener != null)
            listeners.add(listener);
    }

    public synchronized void removeEventListener(EventListener listener) {
        if(listener != null)
            listeners.remove(listener);
    }

    public synchronized void dispatch(IEvent event) {
        for(EventListener listener : listeners)
            listener.onEvent(event);
    }
}
