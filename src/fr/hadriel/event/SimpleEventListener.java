package fr.hadriel.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class SimpleEventListener implements IEventListener {

    private Class<? extends IEvent> type;
    private List<IEventHandler<? extends IEvent>> handlers;

    public SimpleEventListener(Class<? extends IEvent> type) {
        this.type = type;
        this.handlers = new ArrayList<>();
    }

    public synchronized <T extends IEvent> void addHandler(Class<T> type, IEventHandler<T> handler) {
        if(this.type != type)
            throw new IllegalArgumentException("Event type " + type.getSimpleName() + " is Unsupported for SimpleEventListener<" + this.type.getSimpleName() + ">");
        handlers.add(handler);
    }

    public synchronized void removeHandler(IEventHandler handler) {
        handlers.remove(handler);
    }

    public synchronized void clearHandlers() {
        handlers.clear();
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean onEvent(IEvent event) {
        if(type != event.getClass())
            return false;
        for (IEventHandler handler : handlers) {
            if (type == event.getClass() && handler.handle(event))
                return true;
        }
        return false;
    }
}