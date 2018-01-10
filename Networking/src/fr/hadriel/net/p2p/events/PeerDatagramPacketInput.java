package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class PeerDatagramPacketInput extends P2PEvent {

    public byte[] buffer;

    public PeerDatagramPacketInput(Peer peer, byte[] buffer) {
        super(peer);
        this.buffer = buffer;
    }
}