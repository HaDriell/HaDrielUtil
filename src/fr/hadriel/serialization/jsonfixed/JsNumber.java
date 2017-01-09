package fr.hadriel.serialization.jsonfixed;

import fr.hadriel.serialization.SerialException;

/**
 * Created by HaDriel setOn 19/10/2016.
 */
public class JsNumber implements JsPrimitive {

    public double value;

    public JsNumber(double value) {
        this.value = value;
    }

    public byte asByte() {
        return (byte) value;
    }

    public boolean asBoolean() {
        return value != 0;
    }

    public short asShort() {
        return (short) value;
    }

    public char asChar() {
        return (char) value;
    }

    public int asInt() {
        return (int) value;
    }

    public long asLong() {
        return (long) value;
    }

    public float asFloat() {
        return (float) value;
    }

    public double asDouble() {
        return value;
    }

    public String asString() {
        long lv = (long) value;
        if(lv == value) return Long.toString(lv);
        else return Double.toString(value);
    }

    public String toString() {
        return asString();
    }

    public static JsNumber deserialize(String input) throws SerialException {
        return new JsNumber(Json.getJsonNumberValue(input));
    }
}