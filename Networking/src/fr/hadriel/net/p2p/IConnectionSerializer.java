package fr.hadriel.net.p2p;

/**
 *
 * @author glathuiliere
 */
public interface IConnectionSerializer {
    public void onReceive(UDPConnection connection, byte channel, Object object);
    public byte[] onSend(UDPConnection connection, byte channel, Object object);
}