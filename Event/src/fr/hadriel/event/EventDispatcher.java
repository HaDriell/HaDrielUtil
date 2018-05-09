package fr.hadriel.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class EventDispatcher implements IEventListener {

    private final boolean allowMutation;
    private final List<IEventListener> listeners;

    public EventDispatcher() {
        this(false);
    }

    /**
     * @param allowMutation enables Listeners to mute the event while the chain is processing
     */
    public EventDispatcher(boolean allowMutation) {
        this.listeners = new ArrayList<>();
        this.allowMutation = allowMutation;
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
        if (event != null) {
            for (IEventListener listener : listeners) {
                if (allowMutation)
                    event = listener.onEvent(event);
                else
                    listener.onEvent(event);
            }
        }
        return event;
    }
}