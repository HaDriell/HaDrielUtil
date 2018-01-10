package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class PeerConnection extends P2PEvent {

    public PeerConnection(Peer peer) {
        super(peer);
    }
}