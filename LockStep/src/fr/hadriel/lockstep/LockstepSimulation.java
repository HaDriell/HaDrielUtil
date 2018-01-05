package fr.hadriel.lockstep;

import fr.hadriel.event.EventDispatcher;
import fr.hadriel.event.IEventListener;

import java.util.List;

/**
 *
 * @author glathuiliere
 */
public strictfp abstract class LockstepSimulation {

    private EventDispatcher dispatcher;

    public LockstepSimulation() {
        this.dispatcher = new EventDispatcher();
    }

    public void add(IEventListener listener) {
        dispatcher.addEventListener(listener);
    }

    public void remove(IEventListener listener) {
        dispatcher.removeEventListener(listener);
    }

    public void tick(List<ICommand> commands) {
        commands.forEach(dispatcher::onEvent);
    }
}