package fr.hadriel;

import fr.hadriel.net.UDPSocket;
import fr.hadriel.util.Buffer;

import java.net.InetSocketAddress;

/**
 * Created by gauti on 20/12/2017.
 */
public class TestUDPSocket {
    public static final String MESSAGE = "Hello World :)";
    public static void main(String[] args) {
        testUDPSocketBinding(); // Binding mechanism
        testUDPSocketIO(); // Data Sending / Receiving
    }

    public static void testUDPSocketIO() {
        Buffer in = new Buffer(1024);
        Buffer out = new Buffer(1024);
        String message;

        UDPSocket a = new UDPSocket();
        InetSocketAddress aAddress = new InetSocketAddress("127.0.0.1", 6666);
        a.bind(aAddress.getPort());

        UDPSocket b = new UDPSocket();
        InetSocketAddress bAddress = new InetSocketAddress("127.0.0.1", 9999);
        b.bind(bAddress.getPort());

        //A TO B

        //Send Data
        out.clear();
        out.write(MESSAGE.getBytes());
        out.flip();
        a.send(out, bAddress);

        //Receive Data
        in.clear();
        b.receive(in);
        in.flip();

        message = new String(in.array(), 0, in.remaining());
        if(!MESSAGE.equals(message))
            throw new RuntimeException("'" + MESSAGE + "' != '" + message + "'");

        // B TO A

        //Send Data
        out.clear();
        out.write(MESSAGE.getBytes());
        out.flip();
        b.send(out, aAddress);

        //Receive Data
        in.clear();
        a.receive(in);
        in.flip();

        message = new String(in.array(), 0, in.remaining());
        if(!MESSAGE.equals(message))
            throw new RuntimeException("'" + MESSAGE + "' != '" + message + "'");

    }

    public static void testUDPSocketBinding() {
        UDPSocket socket = new UDPSocket();
        if(socket.isBound())
            throw new RuntimeException("UDPSocket should not be bound");
        socket.bind();
        if(!socket.isBound())
            throw new RuntimeException("UDPSocket should be bound");
        socket.unbind();
        if(socket.isBound())
            throw new RuntimeException("UDPSocket should not be bound");

    }
}