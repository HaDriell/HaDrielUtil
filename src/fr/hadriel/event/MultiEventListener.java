package fr.hadriel.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class MultiEventListener implements IEventListener {

    private Map<Class<? extends IEvent>, SimpleEventListener> map;

    public MultiEventListener() {
        this.map = new HashMap<>();
    }

    private synchronized <T extends IEvent> SimpleEventListener getOrCreateListener(Class<T> type) {
        SimpleEventListener result = map.get(type);
        if(result == null) {
            result = new SimpleEventListener(type);
            map.put(type, result);
        }
        return result;
    }

    public synchronized <T extends IEvent> void setHandler(Class<T> type, IEventHandler<T> handler) {
        SimpleEventListener listener = getOrCreateListener(type);
        listener.addHandler(type, handler);
    }

    public synchronized <T extends IEvent> void removeHandler(Class<T> type, IEventHandler<T> handler) {
        SimpleEventListener listener = getOrCreateListener(type);
        listener.removeHandler(handler);
    }

    public synchronized void clearHandlers() {
        map.clear();
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean onEvent(IEvent event) {
        for(Map.Entry<Class<? extends IEvent>, SimpleEventListener> entry : map.entrySet()) {
            Class<? extends IEvent> type = entry.getKey();
            SimpleEventListener listener = entry.getValue();
            if(listener.onEvent(event))
                return true;
        }
        return false;
    }
}