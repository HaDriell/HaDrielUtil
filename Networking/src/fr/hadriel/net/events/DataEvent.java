package fr.hadriel.net.events;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class DataEvent extends NetworkEvent {

    public final byte[] data;
    public final int offset;
    public final int length;

    public DataEvent(DatagramPacket packet) {
        this(packet.getAddress(), packet.getPort(), packet.getData(), packet.getOffset(), packet.getLength());
    }

    public DataEvent(InetAddress address, int port, byte[] data, int offset, int length) {
        super(address, port);
        this.data = data;
        this.offset = offset;
        this.length = length;
    }
}
