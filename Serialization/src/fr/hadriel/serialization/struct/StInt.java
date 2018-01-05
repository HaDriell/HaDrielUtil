package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StInt extends StPrimitive {

    public int value;

    public StInt(int value) {
        super(Struct.TYPE_INT);
        this.value = value;
    }

    protected int getDataSize() {
        return 4;
    }

    protected int serializeData(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StInt deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_INT) return null;
        pointer++;
        int value = Serial.readInt(buffer, pointer);
        return new StInt(value);
    }

    public static StInt deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_INT) return null;
        return new StInt(Serial.readInt(in));
    }

    public String toString() {
        return "StInt(" + value + ")";
    }

    public boolean equals(StPrimitive primitive) {
        return primitive instanceof StInt && ((StInt) primitive).value == value;
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
