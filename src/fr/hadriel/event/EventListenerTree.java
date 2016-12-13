package fr.hadriel.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere setOn 16/08/2016.
 */
public class EventListenerTree implements IEventListener {

    private Map<Class<? extends IEvent>, IEventListener> map;
    private IEventListener defaultListener;

    public EventListenerTree(IEventListener defaultListener) {
        this.map = new HashMap<>();
        this.defaultListener = defaultListener;
    }

    public EventListenerTree() {
        this(null);
    }

    public synchronized void setOnDefault(IEventListener listener) {
        this.defaultListener = listener;
    }

    public synchronized void setOn(Class<? extends IEvent> type, IEventListener listener) {
        map.put(type, listener);
    }

    public synchronized void clearHandlers() {
        map.clear();
        defaultListener = null;
    }

    public synchronized boolean onEvent(IEvent event) {
        IEventListener listener = map.get(event.getClass());
        if(listener != null) return listener.onEvent(event);
        return defaultListener != null && defaultListener.onEvent(event);
    }
}