package fr.hadriel.main.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class ForwardEventListener implements IEventListener {

    private List<IEventListener> listeners;

    public ForwardEventListener() {
        this.listeners = new ArrayList<>();
    }

    public synchronized void addEventListener(IEventListener listener) {
        if(listener != null) {
            listeners.add(listener);
        }
    }

    public synchronized void removeEventListener(IEventListener listener) {
        if(listener != null) {
            listeners.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized IEvent onEvent(IEvent event) {
        for(IEventListener listener : listeners) {
            listener.onEvent(event);
        }
        return event;
    }
}