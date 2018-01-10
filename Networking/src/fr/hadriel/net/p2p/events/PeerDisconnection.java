package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class PeerDisconnection extends P2PEvent {

    public PeerDisconnection(Peer peer) {
        super(peer);
    }
}