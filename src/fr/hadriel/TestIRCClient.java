package fr.hadriel;

import fr.hadriel.events.DataEvent;
import fr.hadriel.net.UDPSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Created by glathuiliere on 17/08/2016.
 */
public class TestIRCClient {
    public static void main(String[] args) throws Exception {
        UDPSocket client = new UDPSocket();
        client.start();
        client.listener.addHandler(DataEvent.class, (event -> {
            System.out.println(event.connection + ": " + new String(event.data, event.dataOffset, event.dataLength));
            return true;
        }));

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Server address:");
        InetAddress address = InetAddress.getByName(in.readLine());
        System.out.print("Server port:");
        int port = Integer.parseInt(in.readLine());
        new Thread(() -> {
            while(!Thread.interrupted()) {
                try {
                    String line = in.readLine();
                    if(line.length() > 0) {
                        client.send(line.getBytes(), address, port);
                    }
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            }
        }, "Client Thread").start();
    }
}
