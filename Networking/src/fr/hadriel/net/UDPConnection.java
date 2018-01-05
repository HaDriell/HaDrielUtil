package fr.hadriel.net;

import java.net.InetAddress;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class UDPConnection extends Connection {

    private UDPHost host;

    public UDPConnection(InetAddress address, int port, UDPHost host) {
        super(address, port);
    }
}
