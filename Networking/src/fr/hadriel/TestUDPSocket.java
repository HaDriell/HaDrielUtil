package fr.hadriel;

import fr.hadriel.event.DelegateEventListener;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventHandler;
import fr.hadriel.net.p2p.Host;
import fr.hadriel.net.p2p.Peer;
import fr.hadriel.net.p2p.events.Packet;
import fr.hadriel.net.p2p.events.Connection;
import fr.hadriel.net.p2p.events.Disconnection;
import fr.hadriel.util.Buffer;

/**
 * Created by gauti on 20/12/2017.
 */
public class TestUDPSocket {
    public static final String MESSAGE = "Hello World :)";
    public static void main(String[] args) {
        //A and B Host initialization & Binding
        Host a = new Host(1000);
        Host b = new Host();
        a.bind();
        b.bind();


        Peer a_to_b = a.connect("localhost", b.port());
        Peer b_to_a = b.connect("localhost", a.port());

        //Event binding for A
        STATIC.BIND(a, Connection.class, STATIC::LOG_EVENT_TYPE);
        STATIC.BIND(a, Packet.class, STATIC::LOG_EVENT_TYPE);
        STATIC.BIND(a, Disconnection.class, STATIC::LOG_EVENT_TYPE);
        STATIC.BIND(a_to_b, Packet.class, STATIC::LOG_DATA);
        STATIC.BIND(a_to_b, Packet.class, STATIC::ECHO_DATA);

        //Event binding for B
        STATIC.BIND(b, Connection.class, STATIC::LOG_EVENT_TYPE);
        STATIC.BIND(b, Packet.class, STATIC::LOG_EVENT_TYPE);
        STATIC.BIND(b, Disconnection.class, STATIC::LOG_EVENT_TYPE);
        STATIC.BIND(b_to_a, Packet.class, STATIC::LOG_DATA);
//        STATIC.BIND(b_to_a, Packet.class, STATIC::ECHO_DATA);

        a_to_b.send(new Buffer("Helloworld".getBytes()));

        try { Thread.sleep(10_000); } catch (InterruptedException ignore) { } // let time for Sockets to bind & exchange data
    }

    public static final class STATIC {
        private static <E extends IEvent> IEvent LOG_EVENT_TYPE(E event) {
            System.out.println(event.getClass().getSimpleName());
            return event;
        }

        private static IEvent LOG_DATA(Packet event) {
            System.out.println(String.format("[%s:%d]: %s", event.peer.getAddress(), event.peer.getPort(), new String(event.buffer().array())));
            return event;
        }

        private static IEvent ECHO_DATA(Packet event) {
            event.peer.send(event.buffer());
            return event;
        }

        private static <T extends IEvent> void BIND(Host host, Class<T> type, IEventHandler<T> handler) {
            host.getDispatcher().addEventListener(new DelegateEventListener(type, handler));
        }

        private static <T extends IEvent> void BIND(Peer peer, Class<T> type, IEventHandler<T> handler) {
            peer.getDispatcher().addEventListener(new DelegateEventListener(type, handler));
        }
    }
}