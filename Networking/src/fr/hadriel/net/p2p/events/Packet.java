package fr.hadriel.net.p2p.events;

import fr.hadriel.net.p2p.Peer;
import fr.hadriel.util.Buffer;

import java.net.DatagramPacket;
import java.util.Arrays;

/**
 *
 * @author glathuiliere
 */
public class Packet extends P2PEvent {

    public final byte[] buffer;

    public Packet(Peer peer, DatagramPacket packet) {
        this(peer, packet.getData(), packet.getOffset(), packet.getLength());
    }

    public Packet(Peer peer, byte[] buffer, int offset, int length) {
        super(peer);
        this.buffer = Arrays.copyOfRange(buffer, offset, offset + length);
    }

    public Buffer buffer() {
        return new Buffer(buffer);
    }
}