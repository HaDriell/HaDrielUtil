package fr.hadriel.net.p2p;

import java.net.InetAddress;

/**
 *
 * @author glathuiliere
 */
public interface INetworkManager {

    /**
     * Network Setting
     * @return the maximum data size of an acceptable DatagramPacket
     */
    public int getMaximumTransferUnit();

    /**
     * Network Setting
     * @return the amount of milliseconds after what an idle UDPConnection is automatically closed locally
     */
    public int getPeerTimeout();

    /**
     * Called when a new UDPConnection starts sending data to the Host
     * @param host the managed Host
     * @param address the remote address
     * @param port the remote port
     * @return true if the connection is accepted
     */
    public boolean accept(Host host, InetAddress address, int port);

    /**
     * Called when a UDPConnection is connected (locally or remotely)
     * @param host the managed Host
     * @param UDPConnection the connected UDPConnection
     */
    public void onConnection(Host host, UDPConnection UDPConnection);

    /**
     * Called when an UDPConnection didn't receive any data for 25% of the Timeout
     * @param host
     * @param connection
     */
    public void onIdle(Host host, UDPConnection connection);

    /**
     * Called when a UDPConnection is disconnected (locally or remotely)
     * @param host the managed Host
     * @param UDPConnection the disconnected UDPConnection
     */
    public void onDisconnection(Host host, UDPConnection UDPConnection);
}