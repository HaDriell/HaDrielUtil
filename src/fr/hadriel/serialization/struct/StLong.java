package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StLong extends StEntry {

    public long value;

    public StLong(long value) {
        super(TYPE_LONG);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 8;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StLong deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_LONG) return null;
        pointer++;
        long value = Serial.readLong(buffer, pointer);
        return new StLong(value);
    }

    public String toString() {
        return "StList(" + value + ")";
    }
}
