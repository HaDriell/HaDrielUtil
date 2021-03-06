package fr.hadriel.util;


import java.io.*;

/**
 * Created by HaDriel on 13/10/2016.
 */
public class IOUtils {

    public static byte[] readStreamBytes(InputStream in, int amount) throws IOException {
        byte[] buffer = new byte[amount];
        int remaining = amount;
        while (remaining > 0)
            remaining -= in.read(buffer, buffer.length - remaining, remaining);
        return buffer;
    }

    public static byte[] readFile(String filename) throws IOException {
        return readFile(new File(filename));
    }

    public static byte[] readFile(File file) throws IOException {
        return readStreamBytes(new FileInputStream(file), (int) file.length());
    }

    public static String readFileAsString(String filename) throws IOException {
        return readStreamAsString(new FileInputStream(filename));
    }

    public static String readFileAsString(File file) throws IOException {
        return readStreamAsString(new FileInputStream(file));
    }

    public static String readStreamAsString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[8192];
        int len;
        try {
            while ((len = in.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, len));
            }
        } catch (IOException ignore) {}
        return sb.toString();
    }

    public static void insert(RandomAccessFile access, byte[] operationBuffer, long pointer, long count) throws IOException {
        long position = access.length();
        access.setLength(access.length() + count);
        while(position > pointer) {
            int size = (int) Math.min(position - pointer, operationBuffer.length); // find the buffer componentSize to use
            access.seek(position - size); //place the pointer
            access.read(operationBuffer, 0, size); // copy io
            access.seek(position - size + count); //replace to the sender
            access.write(operationBuffer, 0, size); // paste io
            position -= size; //shift position back
        } //continue
    }

    public static void cut(RandomAccessFile access, byte[] operationBuffer, long pointer, long count) throws IOException {
        long position = pointer + count;
        long length = access.length();
        while (length > position) {
            int size = (int) Math.min(length - position, operationBuffer.length);
            access.seek(position); //place pointer
            access.read(operationBuffer, 0, size); // copy io
            access.seek(position - count); // rewind to copy right before the block
            access.write(operationBuffer, 0, size);// paste io
            position += size;
        }
        access.setLength(length - count); // fit file setLength
    }
}
