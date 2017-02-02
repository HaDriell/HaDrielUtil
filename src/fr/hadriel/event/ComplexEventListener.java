package fr.hadriel.event;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by glathuiliere on 01/02/2017.
 *
 * This Event Listener can register multiple Handlers for a given Event type.
 */
public class ComplexEventListener {

    private Map<Class<? extends IEvent>, List<IEventHandler>> map;

    public ComplexEventListener() {
        this.map = new HashMap<>();
    }

    public synchronized <T extends IEvent> void addEventHandler(Class<T> type, IEventHandler<T> handler) {
        List<IEventHandler> list = map.get(type);
        if(list == null) {
            list = new ArrayList<>();
            map.put(type, list);
        }
        list.add(handler);
    }

    public synchronized <T extends IEvent> void removeEventHandler(Class<T> type, IEventHandler<T> handler) {
        List<IEventHandler> list = map.get(type);
        if(list != null) {
            list.remove(handler);
        }
    }

    public void clear() {
        map.clear();
    }

    @SuppressWarnings("unchecked")
    public boolean onEvent(IEvent event) {
        List<IEventHandler> list = map.get(event.getClass());
        if(list != null) {
            for(IEventHandler handler : list) {
                if(handler.handle(event))
                    return true;
            }
        }
        return false;
    }
}
