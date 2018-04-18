package fr.hadriel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TestMappedByteBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("Media/res/Animation.fba", "r");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, channel.position(), channel.size());
        System.out.println(buffer.isDirect());
    }
}
