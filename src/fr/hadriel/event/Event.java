package fr.hadriel.event;


/**
 * Created by glathuiliere on 08/08/2016.
 */
public abstract class Event {

    private boolean consumed;
    private boolean listenable;

    protected Event(boolean listenable) {
        this.listenable = listenable;
        this.consumed = false;
    }

    public boolean isListenable() {
        return listenable;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }

    public static <T extends Event> void dispatch(Event event, Class<T> type, IEventHandler<T> handler) {
        if(event.getClass() == type) handler.handle(type.cast(event));
    }
}