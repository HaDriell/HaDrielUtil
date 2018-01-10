package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;

/**
 *
 * @author glathuiliere
 */
public class PeerObjectOutput extends P2PEvent {

    public final Object object;
    public final boolean reliable;

    public PeerObjectOutput(Peer peer, Object object, boolean reliable) {
        super(peer);
        this.object = object;
        this.reliable = reliable;
    }
}