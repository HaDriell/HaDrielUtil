package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StShort extends StPrimitive {

    public short value;

    public StShort(short value) {
        super(Struct.TYPE_SHORT);
        this.value = value;
    }

    protected int getDataSize() {
        return 2;
    }

    protected int serializeData(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StShort deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_SHORT) return null;
        pointer++;
        short value = Serial.readShort(buffer, pointer);
        return new StShort(value);
    }

    public static StShort deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_SHORT) return null;
        return new StShort(Serial.readShort(in));
    }

    public String toString() {
        return "StShort(" + value + ")";
    }

    public boolean equals(StPrimitive primitive) {
        return primitive instanceof StShort && ((StShort) primitive).value == value;
    }

    @Override
    public byte asByte() {
        return (byte) value;
    }

    @Override
    public boolean asBoolean() {
        return value != Serial.FALSE;
    }

    @Override
    public short asShort() {
        return (short) value;
    }

    @Override
    public char asChar() {
        return (char) value;
    }

    @Override
    public int asInt() {
        return (int) value;
    }

    @Override
    public long asLong() {
        return (long) value;
    }

    @Override
    public float asFloat() {
        return (float) value;
    }

    @Override
    public double asDouble() {
        return (double) value;
    }

    @Override
    public String asString() {
        return "" + value;
    }

    public StArray asStArray() {
        throw new UnsupportedOperationException();
    }

    public StObject asStObject() {
        throw new UnsupportedOperationException();
    }
}