package fr.hadriel.serialization.hzip;

import fr.hadriel.serialization.Serial;
import fr.hadriel.util.IO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by HaDriel on 13/10/2016.
 */
public class HZipFile {

    //HZipFile Header
    private static final byte[] PREFIX = "HZIP".getBytes();

    //HZipFile Index data
    private List<Index> indices;

    //HZipFile IO
    private RandomAccessFile access;
    private Lock lock = new ReentrantLock();
    private byte[] buffer;


    //----------------------------------------------------------------------------------------------------------------//
    //ARCHIVE CONSTRUCTORS
    //----------------------------------------------------------------------------------------------------------------//

    public HZipFile() {
        this(1024 * 1024); // 1MB default buffer size
    }

    public HZipFile(int operationBufferSize) {
        this.indices = new ArrayList<>();
        this.buffer = new byte[operationBufferSize];
    }

    //----------------------------------------------------------------------------------------------------------------//
    //INDEX MANIPULATION FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------//

    public Index get(String name) {
        for(Index i : indices)
            if(i.name().equals(name))
                return i;
        return null;
    }

    private long getPointer(Index Index) throws IOException {
        //Check for pointer error
        int i = indices.indexOf(Index);
        if(i < 0) throw new IOException("Index not in Archive");

        //Start before the current Index
        i--;
        //Find Pointer by summing indices sizes before this Index
        long pointer = 4 + 8; //offset by header
        for(; i >= 0; i--) pointer += indices.get(i).length();
        return pointer;
    }

    public Index create(String name, long size) throws IOException {
        //Look for errors
        if(name == null || name.length() == 0 || name.length() > Short.MAX_VALUE)
            throw new IllegalArgumentException("name null or too long");
        if(size < 0)
            throw new IllegalArgumentException("size < 0");

        //Look for existing Index with same name
        Index index = get(name);
        if(index != null)
            return null; // throw error ?

        //Create And Allocate Index
        index = new Index(name, size);
        indices.add(index);
        long pointer = getPointer(index);
        allocate(pointer, index.length());
        saveIndex();
        return index;
    }

    public void delete(String name) throws IOException {
        delete(get(name));
    }

    public void delete(Index index) throws IOException {
        long pointer = getPointer(index);
        deallocate(pointer, index.length());
        indices.remove(index);
        saveIndex();
    }

    public void resize(Index Index, long size) throws IOException {
        long pointer = getPointer(Index);
        long delta = size - Index.length();
        if(delta > 0) deallocate(pointer + size, -delta);
        if(delta < 0) allocate(pointer, delta);
        Index.length(size);
        saveIndex();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //INDEX FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------//

    public long getIndexMemsize() {
        long size = 2;
        for(Index i : indices) {
            size += i.memsize();
        }
        return size;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //FILE RAW FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------//


    public void open(String filename) throws IOException { open(new File(filename));}

    public void open(File file) throws IOException {
        lock.lock();
        try {
            if (access != null) return;
            access = new RandomAccessFile(file, "rw");
            if (access.length() > 8) {
                access.seek(0);
                loadIndex();
            }
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        if(access == null) return;
        lock.lock();
        try { access.close(); } catch (IOException ignore) {}
        finally {
            lock.unlock();
        }
        access = null;
    }

    private void loadIndex() throws IOException {
        lock.lock();
        try {
            //Check File Header
            byte[] prefix = new byte[4];
            access.seek(0);
            access.read(prefix);
            if (!Arrays.equals(prefix, PREFIX)) throw new IOException("Bad File Prefix");

            //Find Index EntryPoint right after the file prefix
            long indexPointer = Serial.readLong(access);

            //Parse Index
            access.seek(indexPointer);
            short indexCount = Serial.readShort(access);
            for (int i = 0; i < indexCount; i++) {
                Index Index = new Index(access);
                indices.add(Index);
            }
        } finally {
            lock.unlock();
        }
    }

    private void saveIndex() throws IOException {
        lock.lock();
        try {
            //Find indexPointer
            long indexPointer = 12;
            if (indices.size() > 0) {
                Index last = indices.get(indices.size() - 1);
                indexPointer = getPointer(last) + last.length();
            }

            //Allocate Size for Index Serialization
            long allocated = (access.length() - indexPointer);
            long allocRequired = getIndexMemsize();
            long delta = allocRequired - allocated;
            if (delta < 0) deallocate(indexPointer + allocRequired, -delta);
            if (delta > 0) allocate(indexPointer, delta);

            //Write Index
            access.seek(indexPointer);
            Serial.write(access, (short) indices.size());
            for (Index Index : indices) {
                Index.save(access);
            }

            //Write Header (lol at last !)
            access.seek(0);
            Serial.write(access, PREFIX);
            Serial.write(access, indexPointer);
        } finally {
            lock.unlock();
        }
    }


    private void allocate(long pointer, long count) throws IOException {
        lock.lock();
        try {
            IO.insert(access, buffer, pointer, count);
        } finally {
            lock.unlock();
        }
    }

    private void deallocate(long pointer, long count) throws IOException {
        lock.lock();
        try {
            IO.cut(access, buffer, pointer, count);
        } finally {
            lock.lock();
        }
    }

    private void writeRaw(long pointer, byte[] buffer, int offset, int length) throws IOException {
        lock.lock();
        try {
            access.seek(pointer);
            Serial.write(access, buffer, offset, length);
        } finally {
            lock.unlock();
        }
    }

    private void readRaw(long pointer, byte[] buffer, int offset, int length) throws IOException {
        lock.lock();
        try {
            access.seek(pointer);
            Serial.readBytes(access, buffer, offset, length);
        } finally {
            lock.unlock();
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    //BASIC INDEX IO FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------//


    public void write(Index index, long filePointer, byte[] buffer) throws IOException {
        write(index, filePointer, buffer, 0, buffer.length);
    }

    public void write(Index index, long filePointer, byte[] buffer, int offset, int length) throws IOException {
        long pointer = getPointer(index);
        if(filePointer < 0) throw new IOException("filePointer < 0");
        if(filePointer + length > index.length()) resize(index, filePointer + length); //insert if file is overflowing
        writeRaw(pointer + filePointer, buffer, offset, length);
    }

    public int read(Index index, long filePointer, byte[] buffer) throws IOException {
        return read(index, filePointer, buffer, 0, buffer.length);
    }

    public int read(Index index, long filePointer, byte[] buffer, int offset, int length) throws IOException {
        long pointer = getPointer(index);
        if(filePointer < 0) throw new IOException("filePointer < 0");
        int readLength = (int) Math.min(length, index.length() - filePointer);
        readRaw(pointer, buffer, offset, readLength);
        return readLength;
    }
}