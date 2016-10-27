package fr.hadriel.serialization.hzip;

import fr.hadriel.serialization.io.BoundedSerialAccess;
import fr.hadriel.serialization.io.SerialFileChannel;
import fr.hadriel.serialization.io.SerialInput;
import fr.hadriel.serialization.io.SerialOutput;
import fr.hadriel.util.memory.ContiguousMemoryManager;
import fr.hadriel.util.memory.Pointer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public class HZipArchive {

    private SerialFileChannel channel;
    private ContiguousMemoryManager manager;
    private Map<String, Pointer> nameMap;

    public HZipArchive(File file) throws IOException {
        this.nameMap = new HashMap<>();
        this.manager = new ContiguousMemoryManager();
        this.channel = new SerialFileChannel(file);

        //TODO : make the callbacks implementations

        if(channel.length() == 0) writeMap();
        readMap();
    }

    private long getMapAllocationSize() {
        long size = 2; //count
        for(Map.Entry<String, Pointer> e : nameMap.entrySet()) { // only target the named pointers
            size += 2 + e.getKey().length() + 8; // short + nameBytes + long;
        }
        return size;
    }

    private void readMap() throws IOException {
        SerialInput in;

        //Read the file Header
        in = new BoundedSerialAccess(channel, 0, 12);
        byte[] prefix = new byte[4];
        in.readBytes(prefix);
        if(!Arrays.equals(prefix, HZip.HEADER_PREFIX))
            throw new IOException("Invalid File Header '" + new String(prefix) + "'");
        long mapAllocationSize = in.readLong();

        //Read the Map
        nameMap.clear();
        in = new BoundedSerialAccess(channel, 12, mapAllocationSize);
        List<Pointer> pointers = new ArrayList<>();
        pointers.add(new Pointer(12)); //push the Headerpointer (unreferenced)
        short count = in.readShort();
        for(int i = 0; i < count; i++) {
            short nl = in.readShort();
            byte[] nb = new byte[nl];
            in.readBytes(nb);
            long size = in.readLong();
            String name = new String(nb);
            Pointer p = new Pointer(size);
            pointers.add(p);
            nameMap.put(name, p);
        }

        //apply map
        manager.initialize(pointers);
    }

    private void writeMap() throws IOException {
        SerialOutput out;
        long mapAllocationSize = getMapAllocationSize();
        //Write Header
        out = new BoundedSerialAccess(channel, 0, 12);
        out.write(HZip.HEADER_PREFIX);
        out.write(mapAllocationSize);
    }
}