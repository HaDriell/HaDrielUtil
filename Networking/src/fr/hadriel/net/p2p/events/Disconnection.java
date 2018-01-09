package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class Disconnection extends P2PEvent {

    public Disconnection(Peer peer) {
        super(peer);
    }
}