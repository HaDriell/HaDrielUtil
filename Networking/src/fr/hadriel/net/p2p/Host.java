package fr.hadriel.net.p2p;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author glathuiliere
 */
public class Host {

    //Manager
    private final INetworkManager manager;

    //Peers Configuration
    private final List<UDPConnection> connections;
    private Lock lock;
    private Thread watcher;

    //Network Configuration
    private DatagramSocket socket;

    public Host(INetworkManager manager) {
        this.manager = Objects.requireNonNull(manager);
        this.connections = new ArrayList<>();
    }

    public void bind() {
        bind(0);
    }

    public void bind(int port) {
        bind(null, port);
    }

    public synchronized void bind(InetAddress address, int port) {
        if(isBound())
            unbind();

        try {
            socket = new DatagramSocket(port, address);

            //watcher setup
            lock = new ReentrantLock(); //lock might be corrupted by an interrupted watcher. Prefer using a new one every binding
            watcher = new Thread(this::watch, "Host Watcher");
            watcher.setDaemon(true);
            watcher.start();
        } catch (Exception e) {
            throw new NetworkException("Unable to bind Host ", e);
        }
    }

    public synchronized void unbind() {
        if(!isBound())
            return;

        //stop socket watcher
        watcher.interrupt();
        watcher = null;

        connections.forEach(UDPConnection::close);
        connections.clear();

        //gracefully close the socket
        try {
            socket.close();
        } catch (Exception ignore) {}
    }

    public UDPConnection getConnection(InetAddress address, int port) {
        UDPConnection connection = null;
        lock.lock();
        for(UDPConnection p : connections) {
            if(p.isTargeting(address, port)) {
                connection = p;
                break;
            }
        }
        lock.unlock();
        return connection;
    }

    public UDPConnection connect(String hostname, int port) {
        try {
            return connect(InetAddress.getByName(hostname), port);
        } catch (UnknownHostException e) {
            throw new NetworkException("Unable to getPeer Host ", e);
        }
    }

    public UDPConnection connect(InetAddress address, int port) {
        UDPConnection connection = getConnection(address, port);
        if(connection == null) {
            connection = new UDPConnection(socket, address, port);
            lock.lock();
            connections.add(connection);
            lock.unlock();
            manager.onConnection(this, connection);
        }
        return connection;
    }

    private void watch() {
        while (isBound()) {
            service(10); // 100 UPS minimum. 10 ms lag when connection was "idle"
            lock.lock();
            connections.forEach(c -> c.update(manager.getPeerTimeout()));
            connections.removeIf(UDPConnection::isClosed);
            lock.unlock();
        }
    }

    private void service(int timeout) {
        DatagramPacket datagram = new DatagramPacket(new byte[manager.getMaximumTransferUnit()], manager.getMaximumTransferUnit());
        try {
            socket.setSoTimeout(timeout);
            socket.receive(datagram);
            InetAddress address = datagram.getAddress();
            int port = datagram.getPort();

            UDPConnection connection = getConnection(address, port);

            //New remote Connection initiated from Network
            if(connection == null && manager.accept(this, address, port)) {
                connection = new UDPConnection(socket, address, port, manager.getPeerTimeout());
                lock.lock();
                connections.add(connection);
                lock.unlock();
                manager.onConnection(this, connection);
            }

            if(connection != null) {
                connection.refresh();
            }


        } catch (SocketTimeoutException ignore) {
            //not a real exception. Just a flaw of Java's Exceptions original design
        } catch (IOException e) {
            throw new NetworkException("Issue while watch Socket", e);
        }
    }

    public synchronized InetAddress address() {
        return socket == null ? null : socket.getLocalAddress();
    }

    public synchronized int port() {
        return socket == null ? -1 : socket.getLocalPort();
    }

    public synchronized boolean isBound() {
        return socket != null && socket.isBound();
    }
}