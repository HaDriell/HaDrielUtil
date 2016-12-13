package fr.hadriel;

import fr.hadriel.util.ByteStream;

/**
 * Created by glathuiliere setOn 07/11/2016.
 */
public class TestByteStream {
    public static void main(String[] args) throws Exception {
        byte[] data = "Helloworld".getBytes();
        ByteStream stream = new ByteStream();

        while(!Thread.interrupted()) {
            stream.write(data);
            stream.read(data);
            stream.printStatus();
            System.out.println(new String(data));
            Thread.sleep(100);
        }
    }
}
