package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StObject extends StEntry implements Iterable<Map.Entry<String, StEntry>> {

    private Map<String, StEntry> members;

    public StObject() {
        super(TYPE_OBJECT);
        this.members = new HashMap<>();
    }

    public StEntry get(String name) {
        return members.get(name);
    }

    public void put(String name, StEntry entry) {
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
        for(Map.Entry<String, StEntry> e : members.entrySet()) {
            size += 2 + e.getKey().length() + e.getValue().getSize();
        }
        return size;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, (short) members.size()); // member count < 65535
        for(Map.Entry<String, StEntry> e : members.entrySet()) {
            pointer = Serial.write(buffer, pointer, (short) e.getKey().length());
            pointer = Serial.write(buffer, pointer, e.getKey().getBytes());
            pointer = e.getValue().serialize(buffer, pointer);
        }
        return pointer;
    }

    public Iterator<Map.Entry<String, StEntry>> iterator() {
        return members.entrySet().iterator();
    }

    public static StObject deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_OBJECT) return null;
        pointer++;
        StObject object = new StObject();
        short count = Serial.readShort(buffer, pointer);
        pointer += 2;
        for(int i = 0; i < count; i++) {
            short length = Serial.readShort(buffer, pointer);
            pointer += 2;
            byte[] string = new byte[length];
            pointer += Serial.readByteArray(buffer, pointer, string, length);
            String key = new String(string);
            StEntry value = StEntry.deserialize(buffer, pointer);
            pointer += value.getSize();
            object.put(key, value);
        }
        return object;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StObject(");
        boolean firstStatement = true;
        for(Map.Entry<String, StEntry> e : members.entrySet()) {
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
}