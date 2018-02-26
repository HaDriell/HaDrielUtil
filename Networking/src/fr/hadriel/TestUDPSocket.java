package fr.hadriel;

import fr.hadriel.udp.p2p.Host;
import fr.hadriel.udp.p2p.UDPConnection;

/**
 * Created by gauti on 20/12/2017.
 */
public class TestUDPSocket {

    private static final class Message {
        public String author;
        public String content;

        private Message() {}
        public Message(String author, String content) {
            this.content = content;
            this.author = author;
        }
    }

    public static void main(String[] args) {
        Host host_0 = new Host();
        Host host_1 = new Host();
        host_0.getSerialization().register(Message.class);
        host_1.getSerialization().register(Message.class);
        host_0.bind();
        host_1.bind();

        UDPConnection UDPConnection_0 = host_0.connect("localhost", host_1.port());
        UDPConnection UDPConnection_1 = host_1.connect("localhost", host_0.port());
    }
}