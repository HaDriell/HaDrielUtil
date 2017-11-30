package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StString extends StPrimitive {

    public String value;

    public StString(String value) {
        super(Struct.TYPE_STRING);
        if(value == null) throw new IllegalArgumentException("value cannot be null");
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
        if(buffer[pointer] != Struct.TYPE_STRING) return null;
        pointer++;
        short length = Serial.readShort(buffer, pointer);
        pointer += 2;
        byte[] string = new byte[length];
        Serial.readByteArray(buffer, pointer, string, length);
        return new StString(new String(string));
    }

    public static StString deserialize(byte dataType, InputStream in) throws IOException {
        if(dataType != Struct.TYPE_STRING) return null;
        short length = Serial.readShort(in);
        byte[] string = new byte[length];
        Serial.readBytes(in, string);
        return new StString(new String(string));
    }

    public String toString() {
        return "StString(" + value + ")";
    }

    @Override
    public byte asByte() {
        return Byte.parseByte(value);
    }

    @Override
    public boolean asBoolean() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public short asShort() {
        return Short.parseShort(value);
    }

    @Override
    public char asChar() {
        return value.charAt(0);
    }

    @Override
    public int asInt() {
        return Integer.parseInt(value);
    }

    @Override
    public long asLong() {
        return Long.parseLong(value);
    }

    @Override
    public float asFloat() {
        return Float.parseFloat(value);
    }

    @Override
    public double asDouble() {
        return Double.parseDouble(value);
    }

    @Override
    public String asString() {
        return "" + value;
    }

    @Override
    public StArray asStArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public StObject asStObject() {
        throw new UnsupportedOperationException();
    }
}