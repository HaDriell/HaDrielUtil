package fr.hadriel.net;

import fr.hadriel.util.Buffer;

import java.net.SocketAddress;

/**
 *
 * @author glathuiliere
 */
public abstract class Socket {

    protected int port;

    public SocketAddress receive(Buffer buffer) {
        return receive(buffer, 0);
    }

    /**
     * @return the current bound port or -1 if not bound
     */
    public int port() {
        return port;
    }

    /**
     * tries to bind the socket to the given port
     */
    public abstract void bind(int port);

    /**
     * @param address the receiver address
     * @param buffer the data buffer
     */
    public abstract void send(SocketAddress address, Buffer buffer);

    /**
     * @param buffer the data buffer
     * @param timeout the maximum amount of milliseconds before the receive function returns (0 = infinite)
     * @return the socketaddress of the sender (or null if no message is received)
     */
    public abstract SocketAddress receive(Buffer buffer, int timeout);
}