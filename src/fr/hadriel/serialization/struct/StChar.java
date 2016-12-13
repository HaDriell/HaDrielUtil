package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere setOn 09/08/2016.
 */
public class StChar extends StPrimitive {

    public char value;

    public StChar(char value) {
        super(Struct.TYPE_CHAR);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 2;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StChar deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_CHAR) return null;
        pointer++;
        char value = Serial.readChar(buffer, pointer);
        return new StChar(value);
    }

    public static StChar deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_CHAR) return null;
        return new StChar(Serial.readChar(in));
    }

    public String toString() {
        return "StChar(" + value + ")";
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
