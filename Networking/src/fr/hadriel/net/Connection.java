package fr.hadriel.net;

import fr.hadriel.event.EventDispatcher;

import java.net.InetAddress;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public abstract class Connection extends EventDispatcher {

    public final InetAddress address;
    public final int port;

    protected Connection(InetAddress address, int port) {
        this.address = Objects.requireNonNull(address);
        this.port = port;
    }

    public boolean targets(InetAddress address, int port) {
        return this.address.equals(address) && this.port == port;
    }

    public boolean equals(Connection connection) {
        return connection.address.equals(address) && connection.port == port;
    }
}