package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class PeerObjectInput extends P2PEvent {

    public Object object;

    public PeerObjectInput(Peer peer, Object object) {
        super(peer);
        this.object = object;
    }
}