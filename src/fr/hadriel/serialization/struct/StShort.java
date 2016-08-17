package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StShort extends StructEntry {

    public short value;

    public StShort(short value) {
        super(TYPE_SHORT);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 2;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StShort deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_SHORT) return null;
        pointer++;
        short value = Serial.readShort(buffer, pointer);
        return new StShort(value);
    }

    public String toString() {
        return "StShort(" + value + ")";
    }
}