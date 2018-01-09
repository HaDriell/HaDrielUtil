package fr.hadriel.net.p2p.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.net.p2p.Peer;

import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public class P2PEvent implements IEvent {
    public final Peer peer;

    protected P2PEvent(Peer peer) {
        this.peer = Objects.requireNonNull(peer);
    }
}