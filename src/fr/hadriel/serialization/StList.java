package fr.hadriel.serialization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StList extends StEntry implements Iterable<StEntry> {

    private List<StEntry> entries;

    public StList() {
        super(TYPE_LIST);
        this.entries = new ArrayList<>();
    }

    public StEntry get(int index) {
        return entries.get(index);
    }

    public void add(StEntry entry) {
        entries.add(entry);
    }

    public void remove(StEntry entry) {
        entries.remove(entry);
    }

    public void remove(int index) {
        entries.remove(index);
    }

    public void clear() {
        entries.clear();
    }

    protected int getSizeImpl() {
        int size = 2;
        for(StEntry entry : entries)
            size += entry.getSize();
        return size;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, (short) entries.size());
        for(StEntry entry : entries) {
            pointer = entry.serialize(buffer, pointer);
        }
        return pointer;
    }

    public Iterator<StEntry> iterator() {
        return entries.iterator();
    }

    public static StList deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_LIST) return null;
        pointer++;
        StList list = new StList();
        short count = Serial.readShort(buffer, pointer);
        pointer += 2;
        for(int i = 0; i < count; i++) {
            StEntry entry = StEntry.deserialize(buffer, pointer);
            pointer += entry.getSize();
            list.add(entry);
        }
        return list;
    }
}