package fr.hadriel.network.unet;

import fr.hadriel.events.DataEvent;
import fr.hadriel.events.UpdateEvent;
import fr.hadriel.util.Callback;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere on 22/08/2016.
 */
public class ConnectionManager {

    private List<Connection> connections;
    private Lock lock = new ReentrantLock();

    private UDPSocket socket;

    public ConnectionManager(UDPSocket socket) {
        this.socket = socket;
        this.connections = new ArrayList<>();
    }

    public void forEachConnection(Callback<Connection> callback) {
        lock.lock();
        for(Connection connection : connections) {
            callback.execute(connection);
        }
        lock.unlock();
    }

    public Connection get(InetAddress address, int port) {
        lock.lock();
        for(Connection connection : connections) {
            if(connection.isTargeting(address, port) && !connection.isClosed())
                return connection;
        }
        lock.unlock();
        return null;
    }

    public Connection create(InetAddress address, int port, int timeout) {
        Connection connection = get(address, port);
        if(connection != null) { //pick secretly an old existing connection
            connection.setTimeout(timeout);
        } else {
            connection = new Connection(address, port, socket, timeout);
            lock.lock();
            connections.add(connection);
            lock.unlock();
        }
        return connection;
    }

    public boolean destroy(InetAddress address, int port) {
        Connection connection = get(address, port);
        if(connection != null) {
            lock.lock();
            connections.remove(connection);
            lock.unlock();
            connection.close();
            return true;
        }
        return false;
    }

    public boolean onUpdate(UpdateEvent event) {
        lock.lock();
        int elapsedMS = (int) (1000F * event.delta);
        for(Connection connection : connections) {
            connection.update(elapsedMS);
        }
        lock.unlock();
        return false;
    }

    //Should be called on ANY data received
    public boolean onData(DataEvent event) {
        Connection connection = get(event.address, event.port);
        connection.resetIdleTime(); // data received
        return false;
    }
}