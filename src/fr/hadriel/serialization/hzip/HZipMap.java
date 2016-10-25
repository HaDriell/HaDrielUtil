package fr.hadriel.serialization.hzip;

import fr.hadriel.serialization.Serial;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 21/10/2016.
 */
public class HZipMap {

    private List<HZipEntry> entries = new ArrayList<>();

    public HZipEntry find(String name) {
        for(HZipEntry e : entries)
            if(e.name().equals(name))
                return e;
        return null;
    }

    public HZipEntry create(String name) {
        if(name == null || name.length() == 0 || name.length() > Short.MAX_VALUE)
            throw new IllegalArgumentException("Invalid name");
        HZipEntry entry = find(name);
        if(entry != null)
            return null;
        entry = new HZipEntry(name, 0);
        entries.add(entry);
        return entry;
    }

    public void delete(HZipEntry entry) {
        entries.remove(entry);
    }

    public long getAbsolutePointer(HZipEntry entry) throws IOException {
        int i = entries.indexOf(entry);
        if(i < 0) throw new IOException("Invalid Entry");
        i--;
        long pointer = HZipHeader.MEMSIZE;
        for(; i >= 0; i--) pointer += entries.get(i).length();
        System.out.println(String.format("P[%s] = %d", entry.name(), entry.length()));
        return pointer;
    }

    public long getMapPointer() {
        long p = HZipHeader.MEMSIZE;
        for(HZipEntry e : entries) p += e.length();
        return p;
    }

    public long memsize() {
        long s = 2; // count
        for(HZipEntry e : entries) s += e.memsize();
        return s;
    }

    public void read(DataInput in) throws IOException {
        entries.clear();
        short count = Serial.readShort(in);
        for(int i = 0; i < count; i++) {
            HZipEntry entry = new HZipEntry(in);
            entries.add(entry);
        }
    }

    public void write(DataOutput out) throws IOException {
        Serial.write(out, (short) entries.size());
        for(HZipEntry entry : entries) entry.write(out);
    }
}