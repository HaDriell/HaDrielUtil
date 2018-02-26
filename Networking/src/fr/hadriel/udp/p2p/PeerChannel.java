package fr.hadriel.udp.p2p;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author glathuiliere
 */
public abstract class PeerChannel {

    public final int id;
    public final UDPConnection UDPConnection;
    private final List<Packet> out;
    private final List<Packet> in;

    public PeerChannel(UDPConnection UDPConnection, int id) {
        this.out = new LinkedList<>();
        this.in = new LinkedList<>();
        this.UDPConnection = UDPConnection;
        this.id = id;
    }

    public synchronized void update() {

    }

    public synchronized void submitInput(Packet object) {
        if(object == null)
            return;
        in.add(object);
    }

    public synchronized void submitOutput(Packet object) {
        if(object == null)
            return;
        out.add(object);
    }
}