package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StBoolean extends StEntry {

    public boolean value;

    public StBoolean(boolean value) {
        super(TYPE_BOOL);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 1;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StBoolean deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_BOOL) return null;
        pointer++;
        boolean value = Serial.readBoolean(buffer, pointer);
        return new StBoolean(value);
    }

    public String toString() {
        return "StBoolean(" + value + ")";
    }
}
