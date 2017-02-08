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
public class DeferredEventInterceptor implements IEventFilter {

    private Map<Class<? extends Event>, List<IEventFilter>> filters;

    public DeferredEventInterceptor() {
        this.filters = new HashMap<>();
    }

    public synchronized <T extends Event> void addEventFilter(Class<T> type, IEventFilter<T> filter) {
        List<IEventFilter> list = filters.get(type);
        if(list == null) {
            list = new ArrayList<>();
            filters.put(type, list);
        }
        list.add(filter);
    }

    public synchronized <T extends Event> void removeEventFilter(Class<T> type, IEventFilter<T> filter) {
        List<IEventFilter> list = filters.get(type);
        if(list != null) {
            list.remove(filter);
        }
    }

    public void clear() {
        filters.clear();
    }

    @SuppressWarnings("unchecked")
    public boolean accept(Event event) {
        if(event.isConsumed()) return false;

        List<IEventFilter> list = filters.get(event.getClass());
        if(list == null) return true;

        //Do not use iterators in order to enable EventHandlers manipulations during the event handling
        for (int i = 0; i < list.size(); i++) {
            if(event.isConsumed() || !list.get(i).accept(event))
                return false;
        }

        return true;
    }
}