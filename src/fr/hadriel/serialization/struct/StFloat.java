package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere setOn 09/08/2016.
 */
public class StFloat extends StPrimitive {

    public float value;

    public StFloat(float value) {
        super(Struct.TYPE_FLOAT);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 4;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StFloat deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_FLOAT) return null;
        pointer++;
        float value = Serial.readFloat(buffer, pointer);
        return new StFloat(value);
    }

    public static StFloat deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_FLOAT) return null;
        return new StFloat(Serial.readFloat(in));
    }

    public String toString() {
        return "StFloat(" + value + ")";
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
