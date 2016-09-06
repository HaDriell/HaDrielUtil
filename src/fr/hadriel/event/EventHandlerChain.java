package fr.hadriel.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 25/08/2016.
 */
public class EventHandlerChain<T extends IEvent> implements IEventHandler<T> {

    private List<IEventHandler<T>> handlers;
    private boolean checkReturnValue;

    public EventHandlerChain(boolean checkReturnValue) {
        this.handlers = new ArrayList<>();
        this.checkReturnValue = checkReturnValue;
    }

    public EventHandlerChain() {
        this(true);
    }

    public synchronized void add(IEventHandler<T> handler) {
        if(!handlers.contains(handler))
            handlers.add(handler);
    }

    public synchronized void remove(IEventHandler<T> handler) {
        handlers.remove(handler);
    }

    public synchronized boolean handle(T event) {
        for(IEventHandler<T> listener : handlers) {
            if(checkReturnValue && listener.handle(event))
                return true;
        }
        return false;
    }
}