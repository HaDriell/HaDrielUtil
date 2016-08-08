package fr.hadriel.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class EventListener {

    private Map<Class, EventHandler> handlers = new HashMap<>();

    public <T extends Event> void setHandler(Class<T> type, EventHandler handler) {
        handlers.put(type, handler);
    }

    public void onEvent(Event event) {
        EventHandler handler = handlers.get(event.getClass());
        if(handler != null)
            handler.handle(event);
    }
}