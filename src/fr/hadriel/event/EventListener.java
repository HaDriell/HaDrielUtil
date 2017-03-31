package fr.hadriel.event;

import java.util.*;

/**
 * Created by glathuiliere on 01/02/2017.
 *
 * This Event Listener can handle any kind of Event as long as it has a Handler set to handle it
 */
public class EventListener {

    private Map<Class, IEventHandler> map;

    public EventListener() {
        this.map = new HashMap<>();
    }

    public synchronized <T extends IEvent> void setEventHandler(Class<T> type, IEventHandler<T> handler) {
        if(handler == null)
            map.remove(type);
        else
            map.put(type, handler);
    }

    public synchronized void clear() {
        map.clear();
    }

    @SuppressWarnings("unchecked")
    public void onEvent(IEvent IEvent) {
        if(IEvent == null)
            return;
        IEventHandler handler = map.get(IEvent.getClass());
        if(handler != null)
            handler.handle(IEvent); // unchecked yet safe callback call
    }
}