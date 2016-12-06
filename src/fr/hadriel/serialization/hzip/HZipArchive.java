package fr.hadriel.serialization.hzip;

import fr.hadriel.serialization.io.*;
import fr.hadriel.util.memory.ContiguousMemoryManager;
import fr.hadriel.util.memory.Pointer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public class HZipArchive {

    private Pointer mapPointer;
    private SerialFileChannel channel;
    private ContiguousMemoryManager manager;
    private Map<String, Pointer> pointerMap;

    public HZipArchive(String filename) throws IOException {
        this(new File(filename));
    }

    public HZipArchive(File file) throws IOException {
        this.pointerMap = new HashMap<>();
        this.manager = new ContiguousMemoryManager();
        this.channel = new SerialFileChannel(file);

        manager.setBeforeAllocate((size) -> {
            try {
                channel.setLength(channel.length() + size);
            } catch (IOException ignore) {}
        });

        manager.setBeforeReallocate((allocOp) -> {
            try {
                Pointer p = allocOp.pointer;
                long nsize = allocOp.nsize;
                long delta = nsize - p.size();
                if(delta > 0) channel.insert(p.address() + p.size(), delta);
                if(delta < 0) channel.remove(p.address() + p.size() + delta, -delta);
            } catch (IOException ignore) {}
        });

        manager.setBeforeDelete((p) -> {
            try {
                channel.remove(p.address(), p.size());
            } catch (IOException ignore) {}
        });
        if(channel.length() == 0) writeMap();
        readMap();
    }

    public boolean exists(String name) {
        return find(name) != null;
    }

    public Pointer find(String name) {
        if(name == null || name.length() == 0) return null; //disallow empty & null names
        return pointerMap.get(name);
    }

    public SerialAccess access(String name) {
        return access(find(name));
    }

    public SerialAccess access(Pointer p) {
        if(p == null || p.manager() != manager) return null; //disallow null/deleted pointers
        return new PointerSerialAccess(channel, p);
    }

    public Pointer create(String name) throws IOException {
        if(name == null || name.length() == 0) return null; //disallow empty & null names
        if(find(name) != null) return null; //disallow duplicates
        Pointer p = manager.allocate(0);
        pointerMap.put(name, p);
        writeMap();
        return p;
    }

    public void resize(Pointer p, long nsize) throws IOException {
        manager.reallocate(p, nsize);
        writeMap();
    }

    public void delete(String name ) throws IOException {
        Pointer p = find(name);
        if(p == null) return;
        manager.delete(p);
        writeMap();
    }

    private long getMapAllocationSizeRequirement() {
        long size = 2; //components
        for(Map.Entry<String, Pointer> e : pointerMap.entrySet()) { // only target the named pointers
            size += 2 + e.getKey().length() + 8; // short + nameBytes + long;
        }
        return size;
    }

    private void readMap() throws IOException {
        List<Pointer> pointers = new ArrayList<>();

        //Read the file Header
        pointers.add(new Pointer(12)); // protect header from IOs
        SerialInput header = new BoundedSerialAccess(channel, 0, 12);
        byte[] prefix = new byte[4];
        header.readBytes(prefix);
        if(!Arrays.equals(prefix, HZip.HEADER_PREFIX))
            throw new IOException("Invalid File Header '" + new String(prefix) + "'");
        long mapAllocationSize = header.readLong();

        //Read the Map
        mapPointer = new Pointer(mapAllocationSize);
        pointers.add(mapPointer); // protect the map from IOs
        pointerMap.clear();
        header = new BoundedSerialAccess(channel, 12, mapAllocationSize);
        short count = header.readShort();
        for(int i = 0; i < count; i++) {
            short nl = header.readShort();
            byte[] nb = new byte[nl];
            header.readBytes(nb);
            long size = header.readLong();
            String name = new String(nb);
            Pointer p = new Pointer(size);
            pointers.add(p);
            pointerMap.put(name, p);
            System.out.println(String.format("<%s,%d>", name, p.size()));
        }

        //apply map
        manager.initialize(pointers);
    }

    private void writeMap() throws IOException {
        SerialOutput out;
        long mapAllocationSize = getMapAllocationSizeRequirement();
        //Write Header
        out = new BoundedSerialAccess(channel, 0, 12);
        out.write(HZip.HEADER_PREFIX);
        out.write(mapAllocationSize);

        out = new BoundedSerialAccess(channel, 12, mapAllocationSize);
        out.write((short) pointerMap.size());
        for(Map.Entry<String, Pointer> e : pointerMap.entrySet()) {
            out.write((short) e.getKey().length());
            out.write(e.getKey().getBytes());
            out.write(e.getValue().size());
        }
    }

    public boolean isOpen() {
        return channel != null;
    }

    public void close() {
        channel.close();
        channel = null;
    }
}