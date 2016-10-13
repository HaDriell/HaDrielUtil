package fr.hadriel.util;


import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by HaDriel on 13/10/2016.
 */
public class IO {

    public static void insert(RandomAccessFile access, byte[] buffer, long pointer, long count) throws IOException {
        long position = access.length();
        access.setLength(access.length() + count);
        while(position > pointer) {
            int size = (int) Math.min(position - pointer, buffer.length); // find the buffer size to use
            access.seek(position - size); //place the pointer
            access.read(buffer, 0, size); // copy data
            access.seek(position - size + count); //replace to the destination
            access.write(buffer, 0, size); // paste data
            position -= size; //shift position back
        } //continue
    }

    public static void cut(RandomAccessFile access, byte[] buffer, long pointer, long count) throws IOException {
        long position = pointer + count;
        long length = access.length();
        while (length > position) {
            int size = (int) Math.min(length - position, buffer.length);
            access.seek(position); //place pointer
            access.read(buffer, 0, size); // copy data
            access.seek(position - count); // rewind to copy right before the block
            access.write(buffer, 0, size);// paste data
            position += size;
        }
        access.setLength(length - count); // fit file length
    }
}