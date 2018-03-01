package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 * Event fired when a Peer is firstly detected by a Host
 * @author glathuiliere
 */
public class PeerConnected extends P2PEvent {


    public PeerConnected(Peer peer) {
        super(peer);
    }
}