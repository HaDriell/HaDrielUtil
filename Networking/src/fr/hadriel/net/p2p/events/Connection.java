package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class Connection extends P2PEvent {

    public Connection(Peer peer) {
        super(peer);
    }
}