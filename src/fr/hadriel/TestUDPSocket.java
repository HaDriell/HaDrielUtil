package fr.hadriel;


import fr.hadriel.net.UDPSocket;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestUDPSocket {

    public static void main(String[] args) throws Exception {
        byte[] payload = "Yolo Swag It Works !".getBytes();

        UDPSocket a = new UDPSocket(InetAddress.getLocalHost(), 6666, 1400) {
            public void onReceive(byte[] data) {
                System.out.println("A RECV: " + new String(data, 0, data.length));
            }
        };

        UDPSocket b = new UDPSocket(InetAddress.getLocalHost(), 7777, 1400) {
            public void onReceive(byte[] data) {
                System.out.println("B RECV : " + new String(data, 0, data.length));
            }
        };
        a.start();
        b.start();
        System.out.println("UDP Sockets started");
        a.send(payload, b.address, b.port);
        b.send(payload, a.address, a.port);
        a.stop();
        b.stop();
    }
}
