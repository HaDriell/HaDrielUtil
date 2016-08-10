package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

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

    public void add(boolean value) {
        entries.add(new StBoolean(value));
    }

    public void add(byte value) {
        entries.add(new StByte(value));
    }

    public void add(short value) {
        entries.add(new StShort(value));
    }

    public void add(char value) {
        entries.add(new StChar(value));
    }

    public void add(int value) {
        entries.add(new StInt(value));
    }

    public void add(long value) {
        entries.add(new StLong(value));
    }

    public void add(float value) {
        entries.add(new StFloat(value));
    }

    public void add(double value) {
        entries.add(new StDouble(value));
    }

    public void add(String value) {
        entries.add(new StString(value));
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StList(");
        boolean firstStatement = true;
        for(StEntry entry : entries) {
            if(firstStatement)
                firstStatement = false;
            else
                sb.append(", ");
            sb.append(entry.toString());
        }
        sb.append(')');
        return sb.toString();
    }
}