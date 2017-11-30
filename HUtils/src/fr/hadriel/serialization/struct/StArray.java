package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StArray extends StPrimitive implements Iterable<StPrimitive> {

    private List<StPrimitive> entries;

    public StArray() {
        super(Struct.TYPE_ARRAY);
        this.entries = new ArrayList<>();
    }

    public int length() {
        return entries.size();
    }

    public StPrimitive get(int index) {
        return entries.get(index);
    }

    public void add(StPrimitive entry) {
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

    public void remove(StPrimitive entry) {
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
        for(StPrimitive entry : entries)
            size += entry.getSize();
        return size;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, (short) entries.size());
        for(StPrimitive entry : entries) {
            pointer = entry.serialize(buffer, pointer);
        }
        return pointer;
    }

    public Iterator<StPrimitive> iterator() {
        return entries.iterator();
    }

    public static StArray deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_ARRAY) return null;
        pointer++;
        StArray list = new StArray();
        short count = Serial.readShort(buffer, pointer);
        pointer += 2;
        for(int i = 0; i < count; i++) {
            StPrimitive entry = Struct.deserialize(buffer, pointer);
            pointer += entry.getSize();
            list.add(entry);
        }
        return list;
    }

    public static StArray deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_ARRAY) return null;
        StArray list = new StArray();
        short count = Serial.readShort(in);
        for(int i = 0; i < count; i++) {
            StPrimitive entry = Struct.deserialize(in);
            list.add(entry);
        }
        return list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StArray(");
        boolean firstStatement = true;
        for(StPrimitive entry : entries) {
            if(firstStatement)
                firstStatement = false;
            else
                sb.append(", ");
            sb.append(entry.toString());
        }
        sb.append(')');
        return sb.toString();
    }


    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    public short asShort() {
        throw new UnsupportedOperationException();
    }

    public char asChar() {
        throw new UnsupportedOperationException();
    }

    public int asInt() {
        throw new UnsupportedOperationException();
    }

    public long asLong() {
        throw new UnsupportedOperationException();
    }

    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    public String asString() {
        throw new UnsupportedOperationException();
    }

    public StArray asStArray() {
        return this;
    }

    public StObject asStObject() {
        throw new UnsupportedOperationException();
    }
}