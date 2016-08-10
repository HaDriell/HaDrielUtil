package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StString extends StEntry {

    public String value;

    public StString(String value) {
        super(TYPE_STRING);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 2 + value.length();
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, (short) value.length());
        pointer = Serial.write(buffer, pointer, value.getBytes());
        return pointer;
    }

    public static StString deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_STRING) return null;
        pointer++;
        short length = Serial.readShort(buffer, pointer);
        pointer += 2;
        byte[] string = new byte[length];
        Serial.readByteArray(buffer, pointer, string, length);
        return new StString(new String(string));
    }

    public String toString() {
        return "StString(" + value + ")";
    }
}