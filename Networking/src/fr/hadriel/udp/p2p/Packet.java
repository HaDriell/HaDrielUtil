package fr.hadriel.udp.p2p;

/**
 *
 * @author glathuiliere
 */
public class Packet {

    public final byte channel;
    public byte[] data;

    public Packet(byte channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }
}