package fr.hadriel.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by glathuiliere on 01/02/2017.
 *
 * This Event Listener can register multiple Handlers for a given Event type.
 */
public class DeferredEventListener implements IEventListener {

    private Map<Class<? extends Event>, List<IEventHandler>> handlers;

    public DeferredEventListener() {
        this.handlers = new HashMap<>();
    }

    public synchronized <T extends Event> void addEventHandler(Class<T> type, IEventHandler<T> handler) {
        List<IEventHandler> list = handlers.get(type);
        if(list == null) {
            list = new ArrayList<>();
            handlers.put(type, list);
        }
        list.add(handler);
    }

    public synchronized <T extends Event> void removeEventHandler(Class<T> type, IEventHandler<T> handler) {
        List<IEventHandler> list = handlers.get(type);
        if(list != null) {
            list.remove(handler);
        }
    }

    public void clear() {
        handlers.clear();
    }

    @SuppressWarnings("unchecked")
    public void onEvent(Event event) {
        List<IEventHandler> list = handlers.get(event.getClass());

        if(list != null) {
            //Do not use iterators in order to enable EventHandlers manipulations during the event handling
            for (int i = 0; i < list.size(); i++) {
                if (event.isConsumed()) return;
                list.get(i).handle(event);
            }
        }
    }
}