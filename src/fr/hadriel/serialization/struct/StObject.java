package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by glathuiliere setOn 09/08/2016.
 */
public class StObject extends StPrimitive implements Iterable<Map.Entry<String, StPrimitive>> {

    private Map<String, StPrimitive> members;

    public StObject() {
        super(Struct.TYPE_OBJECT);
        this.members = new HashMap<>();
    }

    public StPrimitive get(String name) {
        return members.get(name);
    }

    public void put(String name, StPrimitive entry) {
        members.put(name, entry);
    }

    public void put(String name, boolean value) {
        put(name, new StBoolean(value));
    }

    public void put(String name, byte value) {
        put(name, new StByte(value));
    }

    public void put(String name, short value) {
        put(name, new StShort(value));
    }

    public void put(String name, char value) {
        put(name, new StChar(value));
    }

    public void put(String name, int value) {
        put(name, new StInt(value));
    }

    public void put(String name, long value) {
        put(name, new StLong(value));
    }

    public void put(String name, float value) {
        put(name, new StFloat(value));
    }

    public void put(String name, double value) {
        put(name, new StDouble(value));
    }

    public void put(String name, String value) {
        put(name, new StString(value));
    }

    public void remove(String name) {
        members.remove(name);
    }

    public void clear() {
        members.clear();
    }

    protected int getSizeImpl() {
        int size = 2;
        for(Map.Entry<String, StPrimitive> e : members.entrySet()) {
            size += 2 + e.getKey().length() + e.getValue().getSize();
        }
        return size;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, (short) members.size()); // member components < 65535
        for(Map.Entry<String, StPrimitive> e : members.entrySet()) {
            pointer = Serial.write(buffer, pointer, (short) e.getKey().length());
            pointer = Serial.write(buffer, pointer, e.getKey().getBytes());
            pointer = e.getValue().serialize(buffer, pointer);
        }
        return pointer;
    }

    public Iterator<Map.Entry<String, StPrimitive>> iterator() {
        return members.entrySet().iterator();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StObject(");
        boolean firstStatement = true;
        for(Map.Entry<String, StPrimitive> e : members.entrySet()) {
            if(firstStatement)
                firstStatement = false;
            else
                sb.append(", ");
            sb.append("'").append(e.getKey()).append("': ");
            sb.append(e.getValue().toString());
        }
        sb.append(')');
        return sb.toString();
    }

    public static StObject deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_OBJECT) return null;
        pointer++;
        StObject object = new StObject();
        short count = Serial.readShort(buffer, pointer);
        pointer += 2;
        for(int i = 0; i < count; i++) {
            short length = Serial.readShort(buffer, pointer);
            pointer += 2;
            byte[] string = new byte[length];
            pointer = Serial.readByteArray(buffer, pointer, string, length);
            String key = new String(string);
            StPrimitive value = Struct.deserialize(buffer, pointer);
            pointer += value.getSize();
            object.put(key, value);
        }
        return object;
    }

    public static StObject deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_OBJECT) return null;
        StObject object = new StObject();
        short count = Serial.readShort(in);
        for(int i = 0; i < count ; i++) {
            short length = Serial.readShort(in);
            byte[] string = new byte[length];
            Serial.readBytes(in, string);
            String key = new String(string);
            StPrimitive value = Struct.deserialize(in);
            object.put(key, value);
        }
        return object;
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
        throw new UnsupportedOperationException();
    }

    public StObject asStObject() {
        return this;
    }
}