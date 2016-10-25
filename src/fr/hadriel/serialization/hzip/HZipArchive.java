package fr.hadriel.serialization.hzip;

import fr.hadriel.serialization.Serial;
import fr.hadriel.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by HaDriel on 21/10/2016.
 */
public class HZipArchive {
    private static final byte[] EMPTY_HZIP = {
            'H','Z','I','P', // PREFIX
            0, 0, 0, 0, 0, 0, 0, HZipHeader.MEMSIZE, //MapPointer
            0, 0 //MapCount
    };

    private HZipHeader header = new HZipHeader();
    private HZipMap map = new HZipMap();
    private long serializedMapLength;

    private byte[] operationBuffer = new byte[1024 * 1024];
    private RandomAccessFile io;

    public void open(String filename) throws IOException {
        open(new File(filename));
    }

    public void open(File file) throws IOException {
        if(io != null) throw new IOException("Already opened");
        io = new RandomAccessFile(file, "rw");
        if(io.length() == 0) { // Init if empty file
            io.write(EMPTY_HZIP);
        }
        io.seek(0);
        header.read(io);
        io.seek(header.mapPointer);
        map.read(io);
    }

    public void close() {
        if(io == null) return;
        try { io.close(); } catch (IOException ignore) {}
        io = null;
    }

    private void updateMap() throws IOException {
        long mapPointer = map.getMapPointer();
        long memsize = map.memsize();
        long delta = memsize - serializedMapLength; // get the old map size
        if(delta > 0) IOUtils.insert(io, operationBuffer, mapPointer, delta);
        if(delta < 0) IOUtils.cut(io, operationBuffer, mapPointer + delta, -delta); // d is negative
        serializedMapLength = memsize;
        io.seek(mapPointer);
        map.write(io);
    }

    public HZipEntry getEntry(String name) {
        return map.find(name);
    }

    public HZipEntry createEntry(String name) throws IOException {
        HZipEntry entry = map.create(name);
        if(entry == null) return null; //nothing changed, entry already exists
        updateMap();
        return entry;
    }

    public boolean deleteEntry(String name) throws IOException {
        HZipEntry entry = map.find(name);
        if(entry == null) return false; // nothing changed, entry doesn't exist
        long pointer = map.getAbsolutePointer(entry);
        IOUtils.cut(io, operationBuffer, pointer, entry.length());
        map.delete(entry);
        updateMap();
        return true;
    }

    public void setEntryLength(HZipEntry entry, long length) throws IOException {
        resizeEntry(entry, length - entry.length());
    }

    public void resizeEntry(HZipEntry entry, long delta) throws IOException {
        System.out.println("Delta:" + delta);
        if(delta > 0) IOUtils.insert(io, operationBuffer, entry.length(), delta);
        if(delta < 0) IOUtils.cut(io, operationBuffer, entry.length() + delta, -delta); // d is negative
    }

    public void write(HZipEntry entry, long filePointer, byte[] buffer) throws IOException {
        write(entry, filePointer, buffer, 0, buffer.length);
    }

    public void write(HZipEntry entry, long filePointer, byte[] buffer, int offset, int length) throws IOException {
        long pointer = map.getAbsolutePointer(entry) + filePointer;
        System.out.println(pointer);
        long deltaLength = filePointer + length - entry.length();
        if(deltaLength > 0) {
            resizeEntry(entry, deltaLength); //make sure the file is big enough for the write operation
        }
        io.seek(pointer);
        Serial.write(io, buffer, offset, length);
    }

    public int read(HZipEntry entry, long filePointer, byte[] buffer) throws IOException {
        return read(entry, filePointer, buffer, 0, buffer.length);
    }

    public int read(HZipEntry entry, long filePointer, byte[] buffer, int offset, int length) throws IOException {
        int readCount = (int) Math.min(entry.length(), filePointer + length); // find what will be reached first : file's end or read setLength
        Serial.readBytes(io, buffer, offset, readCount);
        return readCount;
    }
}