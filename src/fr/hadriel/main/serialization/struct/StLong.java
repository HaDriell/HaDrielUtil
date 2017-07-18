package fr.hadriel.main.serialization.struct;

import fr.hadriel.main.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StLong extends StPrimitive {

    public long value;

    public StLong(long value) {
        super(Struct.TYPE_LONG);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 8;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StLong deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_LONG) return null;
        pointer++;
        long value = Serial.readLong(buffer, pointer);
        return new StLong(value);
    }

    public static StLong deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_LONG) return null;
        return new StLong(Serial.readInt(in));
    }

    public String toString() {
        return "StLong(" + value + ")";
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
