package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StByte extends StPrimitive {

    public byte value;

    public StByte(byte value) {
        super(Struct.TYPE_BYTE);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 1;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StByte deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_BYTE) return null;
        pointer++;
        byte value = Serial.readByte(buffer, pointer);
        return new StByte(value);
    }

    public static StByte deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_BYTE) return null;
        return new StByte(Serial.readByte(in));
    }

    public String toString() {
        return "StByte(" + value + ")";
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
