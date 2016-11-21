package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StDouble extends StPrimitive {

    public double value;

    public StDouble(double value) {
        super(Struct.TYPE_DOUBLE);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 8;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StDouble deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_DOUBLE) return null;
        pointer++;
        double value = Serial.readDouble(buffer, pointer);
        return new StDouble(value);
    }

    public static StDouble deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_DOUBLE) return null;
        return new StDouble(Serial.readDouble(in));
    }

    public String toString() {
        return "StDouble(" + value + ")";
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
}
