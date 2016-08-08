package fr.hadriel;

import fr.hadriel.event.Event;
import fr.hadriel.event.EventHandler;
import fr.hadriel.event.EventListener;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestEvent {

    private static class CustomEvent implements Event {}

    private static class CustomEventHandler implements EventHandler {
        public void handle(Event event) {
            System.out.println("handling event of type : " + event.getClass());
        }
    }

    public static void main(String[] args) {
        EventListener listener = new EventListener();
        listener.setHandler(CustomEvent.class, new CustomEventHandler());
        listener.onEvent(new Event() {}); // Not handled
        listener.onEvent(new CustomEvent()); // Handled
    }
}