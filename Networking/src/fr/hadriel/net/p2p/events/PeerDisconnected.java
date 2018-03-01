package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 * Event fired when a Peer is disconnected (by any reason)
 * @author glathuiliere
 */
public class PeerDisconnected extends P2PEvent {

    public PeerDisconnected(Peer peer) {
        super(peer);
    }

}