package fr.hadriel.lockstep;

import fr.hadriel.event.EventListenerChain;
import fr.hadriel.event.IEventListener;

import java.util.List;

/**
 *
 * @author glathuiliere
 */
public strictfp abstract class LockstepSimulation {

    private EventListenerChain dispatcher;

    public LockstepSimulation() {
        this.dispatcher = new EventListenerChain();
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