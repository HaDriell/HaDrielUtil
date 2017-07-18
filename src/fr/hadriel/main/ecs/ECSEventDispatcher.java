package fr.hadriel.main.ecs;

import fr.hadriel.main.event.IEvent;
import fr.hadriel.main.event.IEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class ECSEventDispatcher {

    private List<IEventListener> listeners;

    public ECSEventDispatcher() {
        this.listeners = new ArrayList<>();
    }

    public synchronized void addEventListener(IEventListener listener) {
        if(listener != null)
            listeners.add(listener);
    }

    public synchronized void removeEventListener(IEventListener listener) {
        if(listener != null)
            listeners.remove(listener);
    }

    public synchronized void dispatch(IEvent event) {
        for(IEventListener listener : listeners)
            listener.onEvent(event);
    }
}
