package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StBoolean extends StPrimitive {

    public boolean value;

    public StBoolean(boolean value) {
        super(Struct.TYPE_BOOL);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 1;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StBoolean deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_BOOL) return null;
        pointer++;
        boolean value = Serial.readBoolean(buffer, pointer);
        return new StBoolean(value);
    }

    public static StBoolean deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_BOOL) return null;
        return new StBoolean(Serial.readBoolean(in));
    }

    public String toString() {
        return "StBoolean(" + value + ")";
    }

    @Override
    public byte asByte() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public short asShort() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    @Override
    public char asChar() {
        return (char) (value ? Serial.TRUE : Serial.FALSE);
    }

    @Override
    public int asInt() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    @Override
    public long asLong() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    @Override
    public float asFloat() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    @Override
    public double asDouble() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    @Override
    public String asString() {
        return Boolean.toString(value);
    }
}
