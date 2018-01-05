package fr.hadriel.net;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.net.events.NetworkEvent;

import javax.swing.text.html.Option;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author glathuiliere
 */
public class ConnectionManager implements IEventListener {

    private List<Connection> connections;
    private final UDPHost host;

    public ConnectionManager(UDPHost host) {
        this.host = Objects.requireNonNull(host);
    }

    public Connection connect(InetAddress address, int port) {
        return null;
    }

    public void disconnect(Connection connection) {
        connections.remove(connection);
    }

    private Connection getConnection(InetAddress address, int port) {
        Optional<Connection> connection = connections.stream().filter(c -> c.port == port && c.address.equals(address)).findFirst();
        return connection.isPresent() ? connection.get() : null;
    }

    public IEvent onEvent(IEvent event) {
        if(event instanceof NetworkEvent) {

        }
        return event;
    }
}