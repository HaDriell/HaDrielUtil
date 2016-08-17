package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StByte extends StructEntry {

    public byte value;

    public StByte(byte value) {
        super(TYPE_BYTE);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 1;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StByte deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_BYTE) return null;
        pointer++;
        byte value = Serial.readByte(buffer, pointer);
        return new StByte(value);
    }

    public String toString() {
        return "StByte(" + value + ")";
    }
}
